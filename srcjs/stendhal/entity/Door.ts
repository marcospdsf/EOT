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

import { MenuItem } from "../action/MenuItem";
import { Portal } from "./Portal";

declare var marauroa: any;
declare var stendhal: any;

export class Door extends Portal {

	override zIndex = 5000;

	override draw(ctx: CanvasRenderingContext2D) {
		let imagePath = "/data/sprites/doors/" + this["class"] + ".png";
		let image = stendhal.data.sprites.get(imagePath);
		if (image.height) {
			let x = (this["x"] - 1) * 32;
			let y = (this["y"] - 1) * 32;
			let height = image.height / 2;

			var offsetY = height;
			if (this["open"] === "") {
				offsetY = 0;
			}
			ctx.drawImage(image, 0, offsetY, image.width, height, x, y, image.width, height);
		}
	}

	override buildActions(list: MenuItem[]) {
		list.push({
			title: "Look",
			type: "look"
		});
		list.push({
			title: "Use",
			type: "use"
		});
	}

	override isVisibleToAction(_filter: boolean) {
		return true;
	}

	/**
	 * Create the default action for this entity. If the entity specifies a
	 * default action description, interpret it as an action command.
	 */
	override getDefaultAction() {
		return {
			"type": "moveto",
			"x": "" + this["x"],
			"y": "" + this["y"],
			"zone": marauroa.currentZoneName
		};
	}

	override getCursor(_x: number, _y: number) {
		return "url(/data/sprites/cursor/portal.png) 1 3, auto";
	}

}
