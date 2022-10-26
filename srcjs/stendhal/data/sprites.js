/***************************************************************************
 *                   (C) Copyright 2003-2022 - Stendhal                    *
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License as        *
 *   published by the Free Software Foundation; either version 3 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 ***************************************************************************/

"use strict";

var stendhal = window.stendhal = window.stendhal || {};
stendhal.data = stendhal.data || {};

stendhal.data.sprites = {

	knownBrokenUrls: {},
	images: {},
	knownShadows: {
		"24x32": true,
		"32x32": true,
		"32x48": true,
		"32x48_long": true,
		"48x64": true,
		"48x64_float": true,
		"64x48": true,
		"64x64": true,
		"64x85": true,
		"64x96": true,
		"76x64": true,
		"81x96": true,
		"96x96": true,
		"96x128": true,
		"128x96": true,
		"128x170": true,
		"144x128": true,
		"168x224": true,
		"192x192": true,
		"192x192_float": true,
		"192x256": true,
		"320x440": true,
		"ent": true
	},

	get: function(filename) {
		if (!filename) {
			return {};
		}
		if (filename.indexOf("undefined") > -1) {
			if (!stendhal.data.sprites.knownBrokenUrls[filename]) {
				console.log("Broken image path: ", filename, new Error());
			}
			stendhal.data.sprites.knownBrokenUrls[filename] = true;
			return {};
		}
		if (stendhal.data.sprites.images[filename]) {
			stendhal.data.sprites.images[filename].counter++;
			return stendhal.data.sprites.images[filename];
		}
		var temp = new Image;
		temp.counter = 0;
		temp.src = filename;
		stendhal.data.sprites.images[filename] = temp;
		return temp;
	},

	getWithPromise: function(filename) {
		return new Promise((resolve) => {
			if (typeof(stendhal.data.sprites.images[filename]) != "undefined") {
				stendhal.data.sprites.images[filename].counter++;
				resolve(stendhal.data.sprites.images[filename]);
			}

			const image = new Image();
			image.counter = 0;
			stendhal.data.sprites.images[filename] = image;
			image.onload = () => resolve(image);
			image.src = filename;
		});
	},

	/** deletes all objects that have not been accessed since this method was called last time */
	// TODO: call clean on map change
	clean: function() {
		for (var i in stendhal.data.sprites.images) {
			console.log(typeof(i));
			if (typeof(i) == "Image") {
				if (stendhal.data.sprites.images[i].counter > 0) {
					stendhal.data.sprites.images[i].counter--;
				} else {
					delete(stendhal.data.sprites.images[i]);
				}
			}
		}
	},

	/**
	 * Get an image element whose image data is an area of a specified image.
	 * If the area matches the original image, the image itself is returned.
	 * Otherwise <em>a copy</em> of the image data is returned. This is meant
	 * to be used for obtaining the drag image for drag and drop.
	 *
	 * @param image original image
	 * @param width width of the area
	 * @param height height of the area
	 * @param {number=} offsetX optional. left x coordinate of the area
	 * @param {number=} offsetY optional. top y coordinate of the area
	 */
	getAreaOf: function(image, width, height, offsetX, offsetY) {
		offsetX = offsetX || 0;
		offsetY = offsetY || 0;
		if ((image.width === width) && (image.height === height)
				&& (offsetX === 0) && (offsetY === 0)) {
			return image;
		}
		var canvas = document.createElement("canvas");
		canvas.width  = width;
		canvas.height = height;
		var ctx = canvas.getContext("2d");
		ctx.drawImage(image, offsetX, offsetY, width, height, 0, 0, width, height);
		// Firefox would be able to use the canvas directly as a drag image, but
		// Chrome does not. This should work in any standards compliant browser.
		var newImage = new Image();
		newImage.src = canvas.toDataURL("image/png");
		return newImage;
	},

	/**
	 * @param {string} fileName
	 * @param {string} filter
	 * @param {number=} param
	 */
	getFiltered: function(fileName, filter, param) {
		const img = stendhal.data.sprites.get(fileName);
		let filterFn;
		if (typeof(filter) === "undefined"
			|| !(filterFn = stendhal.data.sprites.filter[filter])
			|| !img.complete || img.width === 0 || img.height === 0) {
			return img;
		}
		const filteredName = fileName + " " + filter + " " + param;
		let filtered = stendhal.data.sprites.images[filteredName];
		if (typeof(filtered) === "undefined") {
			const canvas = document.createElement("canvas");
			canvas.width  = img.width;
			canvas.height = img.height;
			const ctx = canvas.getContext("2d");
			ctx.drawImage(img, 0, 0);
			const imgData = ctx.getImageData(0, 0, img.width, img.height);
			const data = imgData.data;
			filterFn(data, param);
			ctx.putImageData(imgData, 0, 0);
			canvas.complete = true;
			stendhal.data.sprites.images[filteredName] = filtered = canvas;
		}

		return filtered;
	},

	/**
	 * @param {string} fileName
	 * @param {string} filter
	 * @param {number=} param
	 */
	getFilteredWithPromise: function(fileName, filter, param) {
		const imgPromise = stendhal.data.sprites.getWithPromise(fileName);
		return imgPromise.then(function (img) {
			let filterFn;
			if (typeof(filter) === "undefined"
				|| !(filterFn = stendhal.data.sprites.filter[filter])
				|| !img.complete || img.width === 0 || img.height === 0) {
				return img;
			}
			const filteredName = fileName + " " + filter + " " + param;
			let filtered = stendhal.data.sprites.images[filteredName];
			if (typeof(filtered) === "undefined") {
				const canvas = document.createElement("canvas");
				canvas.width  = img.width;
				canvas.height = img.height;
				const ctx = canvas.getContext("2d");
				ctx.drawImage(img, 0, 0);
				const imgData = ctx.getImageData(0, 0, img.width, img.height);
				const data = imgData.data;
				filterFn(data, param);
				ctx.putImageData(imgData, 0, 0);
				canvas.complete = true;
				stendhal.data.sprites.images[filteredName] = filtered = canvas;
			}

			return filtered;
		});
	},


	/** Image filters */
	filter: {
		// Helper functions
		/**
		 * @param {Number} rgb
		 * @return {Array<Number>}
		 */
		splitrgb: function(rgb) {
			rgb &= 0xffffff;
			var b = rgb & 0xff;
			rgb >>>= 8;
			var g = rgb & 0xff;
			rgb >>>= 8;
			return [rgb, g, b];
		},

		mergergb: function(rgbArray) {
			const r = rgbArray[0] << 16;
			const g = rgbArray[1] << 8;
			return 0xffffff & (r | g | rgbArray[2]);
		},

		/**
		 * @param {Array<Number>} rgb
		 * @return {Array<Number>}
		 */
		rgb2hsl: function(rgb) {
			var r = rgb[0] / 255;
			var g = rgb[1] / 255;
			var b = rgb[2] / 255;

			var max, min, maxVar;
			// Find the max and minimum colors, and remember which one it was
			if (r > g) {
				max = r;
				min = g;
				maxVar = 0;
			} else {
				max = g;
				min = r;
				maxVar = 1;
			}
			if (b > max) {
				max = b;
				maxVar = 2;
			} else if (b < min) {
				min = b;
			}

			// lightness
			var l = (max + min) / 2;
			var s, h;

			// saturation
			var diff = max - min;
			if (diff < 0.000001) {
				s = 0;
				// hue not really defined, but set it to something reasonable
				h = 0;
			} else {
				if (l < 0.5) {
					s = diff / (max + min);
				} else {
					s = diff / (2 - max - min);
				}

				// hue
				if (maxVar === 0) {
					h = (g - b) / diff;
				} else if (maxVar === 1) {
					h = 2 + (b - r) / diff;
				} else {
					h = 4 + (r - g) / diff;
				}
				// Normalize to range [0, 1]. It's more useful than the usual 360
				h /= 6;
			}

			return [h, s, l];
		},

		/**
		 * @param {Array<Number>} hsl
		 * @return {Array<Number>}
		 */
		hsl2rgb: function(hsl) {
			var r, g, b;
			var h = hsl[0];
			var s = hsl[1];
			var l = hsl[2];

			if (s < 0.0000001) {
				r = g = b = Math.floor(255 * l);
			} else {
				var tmp1, tmp2;
				if (l < 0.5) {
					tmp1 = l * (1 + s);
				} else {
					tmp1 = l + s - l * s;
				}
				tmp2 = 2 * l - tmp1;

				var rf = stendhal.data.sprites.filter.hue2rgb(this.limitHue(h + 1/3), tmp2, tmp1);
				var gf = stendhal.data.sprites.filter.hue2rgb(this.limitHue(h), tmp2, tmp1);
				var bf = stendhal.data.sprites.filter.hue2rgb(this.limitHue(h - 1/3), tmp2, tmp1);

				r = Math.floor(255 * rf) & 0xff;
				g = Math.floor(255 * gf) & 0xff;
				b = Math.floor(255 * bf) & 0xff;
			}

			return [r, g, b];
		},

		/**
		 * @param {Number} hue
		 * @param {Number} val1
		 * @param {Number} val2
		 */
		hue2rgb: function(hue, val1, val2) {
			var res = hue;
			if (6 * hue < 1) {
				res = val1 + (val2 - val1) * 6 * hue;
			} else if (2 * hue < 1) {
				res = val2;
			} else if (3 * hue < 2) {
				res = val1 + (val2 - val1) * (2/3 - hue) * 6;
			} else {
				res = val1;
			}

			return res;
		},

		/**
		 * @param {Number} hue
		 */
		limitHue: function(hue) {
			var res = hue;
			if (res < 0) {
				res += 1;
			} else if (res > 1) {
				res -= 1;
			}
			return res;
		}
	},

	/**
	 * Retrieves a shadow sprite if the style is available.
	 *
	 * @param shadowStyle
	 *     Style of shadow to get from cache.
	 * @return
	 *     Image sprite or <code>undefined</code>.
	 */
	getShadow: function(shadowStyle) {
		if (this.knownShadows[shadowStyle]) {
			const img = new Image();
			img.src = "/data/sprites/shadow/" + shadowStyle + ".png";
			return img;
		}

		return undefined;
	},

	/**
	 * Checks if there is a "safe" image available for sprite.
	 *
	 * @param filename
	 *     The sprite image base file path.
	 * @return
	 *     <code>true</code> if a known safe image is available.
	 */
	hasSafeImage: function(filename) {
		return this.knownSafeSprites[filename] == true;
	}
}

// *** Image filters. Prevent the closure compiler from mangling the names. ***
stendhal.data.sprites.filter['trueColor'] = function(data, color) {
	var hslColor = stendhal.data.sprites.filter.rgb2hsl(stendhal.data.sprites.filter.splitrgb(color));
	var end = data.length;
	for (var i = 0; i < end; i += 4) {
		var rgb = [data[i], data[i + 1], data[i + 2]];
		var hsl = stendhal.data.sprites.filter.rgb2hsl(rgb);
		// Adjust the brightness
		var adj = hslColor[2] - 0.5; // [-0.5, 0.5]
		var tmp = hsl[2] - 0.5; // [-0.5, 0.5]
		// tweaks the middle lights either upward or downward, depending
		// on if source lightness is high or low
		var l = hsl[2] - 2.0 * adj * ((tmp * tmp) - 0.25);
		var resultHsl = [hslColor[0], hslColor[1], l];
		var resultRgb = stendhal.data.sprites.filter.hsl2rgb(resultHsl);
		data[i] = resultRgb[0];
		data[i+1] = resultRgb[1];
		data[i+2] = resultRgb[2];
	}
}

// alternatives for known images that may be considered violent or mature
stendhal.data.sprites.knownSafeSprites = {
	"/data/sprites/monsters/huge_animal/thing": true,
	"/data/sprites/monsters/mutant/imperial_mutant": true,
	"/data/sprites/monsters/undead/bloody_zombie": true,
	"/data/sprites/npc/deadmannpc": true
}

stendhal.data.sprites.animations = {
	idea: {
		"love": {delay: 100, offsetX: 24, offsetY: -8}
	}
}
