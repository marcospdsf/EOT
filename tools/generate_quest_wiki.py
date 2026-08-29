import os
import shutil
import re
import sys

sys.stdout.reconfigure(encoding='utf-8')

WIKI_DIR = "c:/Projetos/Stendhal/StendhalWebsiteOpenSource/data/wiki"
QUEST_DIR = os.path.join(WIKI_DIR, "quest")

# 1. Clean existing generated quest directory
if os.path.exists(QUEST_DIR):
    shutil.rmtree(QUEST_DIR)
os.makedirs(QUEST_DIR, exist_ok=True)

# 2. Define High-Quality Custom Quest metadata for Empire of Thorns
custom_quests = [
    # Dragon Trials
    {
        "slug": "kill_dragons",
        "title": "Kill Dragons (16 Nightmare Stages)",
        "region": "Dragon Lair & High Mountains",
        "subregion": "Dragon Sanctum",
        "npc": "Antithei",
        "location": "Kościelisko (0_kościelisko_e)",
        "min_level": "150",
        "story": "Antithei in Kościelisko charges elite warriors with ascending through 16 grueling dragon trials. Each stage challenges you to defeat legendary dragons solo, unlocking immense draconic power.",
        "steps": [
            "Travel to eastern Kościelisko and speak with Antithei about your #readiness.",
            "Defeat Stage 1: Red Dragon to prove your martial strength.",
            "Progress through increasingly ferocious dragon species up to Stage 16 (Ancient Infernal Dragons).",
            "Return to Antithei after completing all 16 stages to receive the ultimate reward."
        ],
        "rewards": ["1,000,000+ XP", "+5,000 Karma", "Draconic Mastery Title", "Mythic Dragon Slayer Gear"]
    },
    {
        "slug": "dead_wife",
        "title": "A Wife's Dying Wish",
        "region": "Highland Regions",
        "subregion": "Zakopane",
        "npc": "Janusz",
        "location": "Zakopane Forest",
        "min_level": "80",
        "story": "Janusz mourns the untimely loss of his beloved wife. He desperately seeks a brave adventurer to retrieve her lost heirloom from dangerous crypt fiends.",
        "steps": [
            "Speak with Janusz in the outskirts of Zakopane.",
            "Venture into the haunted graveyard catacombs.",
            "Recover the silver locket from the crypt horrors.",
            "Return the locket to Janusz to bring peace to his heart."
        ],
        "rewards": ["150,000 XP", "+500 Karma", "Janusz's Blessed Amulet"]
    },
    {
        "slug": "where_dragon",
        "title": "Where Is the Dragon?",
        "region": "Dragon Lair & High Mountains",
        "subregion": "Kościelisko Peaks",
        "npc": "Antithei",
        "location": "Kościelisko",
        "min_level": "120",
        "story": "Villagers report tremors shaking the mountains. Antithei needs someone to scout the high peaks and find where the primordial dragon sleeps.",
        "steps": [
            "Speak with Antithei in Kościelisko about rumors of dragon activity.",
            "Scout the northern ridge for scorch marks and giant claw prints.",
            "Locate the entrance to the hidden volcanic crevasse.",
            "Report your findings back to Antithei."
        ],
        "rewards": ["80,000 XP", "+300 Karma", "Dragon Tracker Charm"]
    },
    {
        "slug": "dragons_guard",
        "title": "The Dragon's Guard",
        "region": "Dragon Lair & High Mountains",
        "subregion": "Dragon Gates",
        "npc": "Dragon Sentry",
        "location": "Dragon Peaks",
        "min_level": "140",
        "story": "An ancient dragon guard blocks the path to the inner vault. Only those who prove their mastery over elemental forces may pass.",
        "steps": [
            "Approach the Dragon Sentry at the high mountain gate.",
            "Acquire an Elemental Crystal from cave guardians.",
            "Present the crystal to prove your worth.",
            "Gain passage into the inner dragon domains."
        ],
        "rewards": ["120,000 XP", "Dragon Gate Key", "+400 Karma"]
    },
    {
        "slug": "clear_quest_pierscien",
        "title": "Purification of the Ring",
        "region": "Highland Regions",
        "subregion": "Kościelisko",
        "npc": "Wojciech",
        "location": "Kościelisko Meadow",
        "min_level": "90",
        "story": "A corrupted signet ring threatens to curse its wearer. Seek holy spring waters and mystical essences to cleanse its dark aura.",
        "steps": [
            "Speak with Wojciech in Kościelisko.",
            "Gather fresh spring water from the high alpine peaks.",
            "Perform the cleansing ritual at the highland altar.",
            "Return the purified ring to Wojciech."
        ],
        "rewards": ["100,000 XP", "+400 Karma", "Purified Highland Signet Ring"]
    },
    # Highland Quests
    {
        "slug": "herbs_for_jadzka",
        "title": "Herbs for Gaździna Jadźka",
        "region": "Highland Regions",
        "subregion": "Zakopane Town",
        "npc": "Gaździna Jadźka",
        "location": "Zakopane Town",
        "min_level": "30",
        "story": "The town healer, Gaździna Jadźka, has run out of vital medicinal mountain herbs required to treat injured villagers.",
        "steps": [
            "Speak with Gaździna Jadźka east of the Zakopane Town Hall.",
            "Gather mountain arnica and alpine herbs from the high meadows.",
            "Deliver the fresh herbs to Jadźka before they dry out."
        ],
        "rewards": ["25,000 XP", "+200 Karma", "5x Potent Restorative Brews"]
    },
    {
        "slug": "pomoc_chlopcowi",
        "title": "Adaś's Infested Cellar",
        "region": "Highland Regions",
        "subregion": "Zakopane Town",
        "npc": "Adaś",
        "location": "Zakopane Cottage",
        "min_level": "20",
        "story": "Young Adaś is terrified because vicious cavern rats have overrun the cellar of his family house.",
        "steps": [
            "Visit young Adaś in his house in Zakopane.",
            "Agree to help and enter the dark basement.",
            "Defeat all cellar vermin and eliminate the nest.",
            "Speak with Adaś to give him the good news."
        ],
        "rewards": ["15,000 XP", "+150 Karma", "Adaś's Lucky Coin", "Fresh Cheese Wheels"]
    },
    {
        "slug": "prozba_wielkoluda",
        "title": "The Friendly Giant's Request",
        "region": "Highland Regions",
        "subregion": "Kościelisko Ridges",
        "npc": "The Giant",
        "location": "Kościelisko Hill",
        "min_level": "70",
        "story": "A gentle giant living atop the rolling hills of Kościelisko is being harassed by aggressive mountain rock-throwers and rogue penitents.",
        "steps": [
            "Climb the northeastern hill in Kościelisko and speak with the Giant.",
            "Hunt down the hostile mountain penitents causing trouble.",
            "Clear rolling boulder hazards along the ridge.",
            "Return to the Giant to receive his hearty thanks."
        ],
        "rewards": ["75,000 XP", "+350 Karma", "Giant's Heavy Stone Ring"]
    },
    {
        "slug": "zagadki_brzezdoma",
        "title": "Riddles of the Wandering Sage",
        "region": "Highland Regions",
        "subregion": "Kościelisko Foothills",
        "npc": "Brzezdom",
        "location": "Kościelisko Foothills",
        "min_level": "40",
        "story": "An enigmatic hermit sitting by the highland path tests the wits of passing travelers with ancient Slavic folklore riddles.",
        "steps": [
            "Speak with Brzezdom and ask for a #riddle.",
            "Answer his questions regarding local legends, peaks, and kings.",
            "Solve all 4 riddles without failing."
        ],
        "rewards": ["45,000 XP", "+300 Karma", "Tome of Highland Wisdom"]
    },
    {
        "slug": "zloty_rog",
        "title": "The Golden Bugle",
        "region": "Highland Regions",
        "subregion": "Zakopane Heights",
        "npc": "Bartłomiej",
        "location": "Zakopane Heights",
        "min_level": "110",
        "story": "Bartłomiej speaks of the legendary Golden Bugle whose sound can awaken mountain defenders in times of great peril.",
        "steps": [
            "Speak with Bartłomiej in Zakopane.",
            "Prove your worth by defeating an archangel or dark fiend.",
            "Gather rare alpine metals and ancient polish wood.",
            "Craft the legendary Golden Bugle."
        ],
        "rewards": ["200,000 XP", "+600 Karma", "The Golden Bugle (Special Horn)"]
    },
    {
        "slug": "zakopane_bank",
        "title": "The Zakopane Treasury Vault",
        "region": "Highland Regions",
        "subregion": "Zakopane Bank",
        "npc": "Kazimierz",
        "location": "Zakopane Bank",
        "min_level": "50",
        "story": "Banker Kazimierz requires security assistance to inspect the underground bank vaults and establish a regional trade line.",
        "steps": [
            "Speak with Kazimierz inside the Zakopane Bank.",
            "Pay the vault inspection fee and clear subterranean disturbances.",
            "Secure the inner safety lockers."
        ],
        "rewards": ["50,000 XP", "+250 Karma", "Free Bank Transfer Certificate"]
    },
    {
        "slug": "zakopane_clouds",
        "title": "Realm in the Clouds",
        "region": "Highland Regions",
        "subregion": "Tatra Cloud Realm",
        "npc": "Cloud Warden",
        "location": "High Tatra Peaks",
        "min_level": "130",
        "story": "A magical bridge made of thick mist leads into the clouds above the Tatra mountains, where celestial creatures guard ancient secrets.",
        "steps": [
            "Ascend to the highest Tatra cliffside and speak with the Cloud Warden.",
            "Navigate the mist portals to reach the cloud sanctuary.",
            "Overcome the sky elementals guarding the celestial altar."
        ],
        "rewards": ["180,000 XP", "+500 Karma", "Featherlight Cloud Boots"]
    },
    # Kraków & Wawel
    {
        "slug": "wawel_brama",
        "title": "Wawel Castle Gates",
        "region": "Kraków & Royal Lands",
        "subregion": "Wawel Castle",
        "npc": "Royal Guard",
        "location": "Wawel Gate",
        "min_level": "60",
        "story": "The royal guard at Wawel Castle inspects all entrants. Show deference to the King and prove your loyalty to gain entry.",
        "steps": [
            "Approach the royal guard at the entrance of Wawel Castle.",
            "Declare your allegiance and answer the guard's questions.",
            "Bow before the King upon entering the courtyard."
        ],
        "rewards": ["35,000 XP", "Wawel Entry Pass", "+200 Karma"]
    },
    {
        "slug": "cataclysm",
        "title": "Cataclysm of Kraków",
        "region": "Kraków & Royal Lands",
        "subregion": "Kraków Underworld",
        "npc": "Kraków Scholar",
        "location": "Kraków City",
        "min_level": "150",
        "story": "Dark tremors threaten to collapse the foundations of Kraków. A scholar warns that an ancient beast awakens below.",
        "steps": [
            "Meet the scholar in Kraków City.",
            "Investigate the subterranean fissures beneath the main square.",
            "Defeat the primordial earth fiends causing the collapse.",
            "Seal the rift using blessed runestones."
        ],
        "rewards": ["300,000 XP", "+1,000 Karma", "Kraków Defender's Crest"]
    },
    {
        "slug": "brama_zrc",
        "title": "Gate of the Shadow Knights",
        "region": "Kraków & Royal Lands",
        "subregion": "Shadow Order Outpost",
        "npc": "Shadow Sentry",
        "location": "Shadow Sanctuary",
        "min_level": "100",
        "story": "The secretive Order of Shadow Knights guards their secluded sanctuary. Prove your courage in the dark to enter.",
        "steps": [
            "Find the hidden trail to the Shadow Order Outpost.",
            "Speak with the Shadow Sentry and accept the trial of shadows.",
            "Survive the trial without light."
        ],
        "rewards": ["120,000 XP", "Shadow Insignia", "+350 Karma"]
    },
    # Combat Mastery Stat Trials
    {
        "slug": "atk_five_hundred",
        "title": "Attack Milestone: 500 ATK",
        "region": "Combat Mastery & Stat Trials",
        "subregion": "Combat Academy",
        "npc": "Master Swordmaster",
        "location": "Training Dojo",
        "min_level": "100",
        "story": "Push your physical offensive capabilities to the absolute limit. Reach 500 Attack power and claim permanent combat enhancements.",
        "steps": [
            "Achieve the required strength and combat prowess.",
            "Bring your attack training slips to the Master Swordmaster.",
            "Receive permanent attack enhancement."
        ],
        "rewards": ["50,000 XP", "+10 Permanent ATK Bonus"]
    },
    {
        "slug": "atk_thousand",
        "title": "Attack Milestone: 1,000 ATK",
        "region": "Combat Mastery & Stat Trials",
        "subregion": "Combat Academy",
        "npc": "Master Swordmaster",
        "location": "Training Dojo",
        "min_level": "150",
        "story": "Ascend to elite warrior rank by mastering advanced offensive combat forms up to 1,000 Attack.",
        "steps": [
            "Demonstrate mastery of advanced weapon arts.",
            "Present your tier-2 attack certificate to the Swordmaster."
        ],
        "rewards": ["150,000 XP", "+25 Permanent ATK Bonus"]
    },
    {
        "slug": "atk_two_thousand",
        "title": "Attack Milestone: 2,500 ATK",
        "region": "Combat Mastery & Stat Trials",
        "subregion": "Combat Academy",
        "npc": "Grand Champion",
        "location": "Grand Arena",
        "min_level": "250",
        "story": "Only the fiercest champions in the realm can achieve 2,500 Attack. Complete the master trial to elevate your striking force.",
        "steps": [
            "Defeat elite arena challengers solo.",
            "Claim your Grand Champion's offensive blessing."
        ],
        "rewards": ["500,000 XP", "+50 Permanent ATK Bonus"]
    },
    {
        "slug": "atk_five_thousand",
        "title": "Attack Milestone: 5,000 ATK (Transcendent)",
        "region": "Combat Mastery & Stat Trials",
        "subregion": "Hall of Immortals",
        "npc": "Grand Marshal",
        "location": "Apex Sanctuary",
        "min_level": "400",
        "story": "Transcendent martial mastery. Attain 5,000 Attack to permanently etch your name in the annals of legendary warriors.",
        "steps": [
            "Complete the ultimate trial of offensive combat.",
            "Attain the pinnacle title and maximum stat empowerment."
        ],
        "rewards": ["2,000,000 XP", "+100 Permanent ATK Bonus", "Warlord's Aura"]
    },
    {
        "slug": "def_five_hundred",
        "title": "Defense Milestone: 500 DEF",
        "region": "Combat Mastery & Stat Trials",
        "subregion": "Shieldhall",
        "npc": "Master Defender",
        "location": "Garrison Hall",
        "min_level": "100",
        "story": "Hone your defensive instincts and resilience to withstand blows that would fell lesser warriors.",
        "steps": [
            "Fortify your armor and reach 500 Defense.",
            "Present defense training slips to the Shieldmaster."
        ],
        "rewards": ["50,000 XP", "+10 Permanent DEF Bonus"]
    },
    {
        "slug": "def_thousand",
        "title": "Defense Milestone: 1,000 DEF",
        "region": "Combat Mastery & Stat Trials",
        "subregion": "Shieldhall",
        "npc": "Master Defender",
        "location": "Garrison Hall",
        "min_level": "150",
        "story": "Achieve 1,000 Defense power to become an immovable bastion on the battlefield.",
        "steps": [
            "Endure grueling endurance trials.",
            "Receive defensive fortification from the master."
        ],
        "rewards": ["150,000 XP", "+25 Permanent DEF Bonus"]
    },
    {
        "slug": "def_two_thousand",
        "title": "Defense Milestone: 2,500 DEF",
        "region": "Combat Mastery & Stat Trials",
        "subregion": "Shieldhall",
        "npc": "Iron Titan",
        "location": "Iron Bastion",
        "min_level": "250",
        "story": "Become as unyielding as mountain granite with 2,500 Defense power.",
        "steps": [
            "Withstand the barrage of heavy siege fiends.",
            "Claim the Iron Titan's permanent blessing."
        ],
        "rewards": ["500,000 XP", "+50 Permanent DEF Bonus"]
    },
    {
        "slug": "def_five_thousand",
        "title": "Defense Milestone: 5,000 DEF (Transcendent)",
        "region": "Combat Mastery & Stat Trials",
        "subregion": "Hall of Immortals",
        "npc": "Grand Bastion",
        "location": "Apex Sanctuary",
        "min_level": "400",
        "story": "The ultimate defensive milestone. No physical blade can pierce your defenses once you attain 5,000 DEF.",
        "steps": [
            "Complete the final trial of invulnerability.",
            "Receive transcendent defensive mastery."
        ],
        "rewards": ["2,000,000 XP", "+100 Permanent DEF Bonus", "Aegis of the Immortals"]
    },
    # Noble Orders & Rings
    {
        "slug": "pierscien_barona",
        "title": "The Baron's Signet Ring",
        "region": "Noble Orders & Relics",
        "subregion": "Nobility Quarter",
        "npc": "Baron Heinrich",
        "location": "Baron's Estate",
        "min_level": "50",
        "story": "Baron Heinrich seeks a trusted knight to secure his family crest and recover the ancestral Baron Signet Ring.",
        "steps": [
            "Speak with Baron Heinrich at his manor.",
            "Track down the thief who robbed the estate.",
            "Recover and return the Baron's Signet Ring."
        ],
        "rewards": ["60,000 XP", "+300 Karma", "Baron's Signet Ring"]
    },
    {
        "slug": "pierscien_rycerza",
        "title": "The Knight's Signet Ring",
        "region": "Noble Orders & Relics",
        "subregion": "Knight's Chapter",
        "npc": "Sir Roland",
        "location": "Knight's Hall",
        "min_level": "80",
        "story": "Sir Roland of the royal guard offers knighthood and the Knight's Ring to champions who prove chivalry and martial honor.",
        "steps": [
            "Undertake the Knight's vow with Sir Roland.",
            "Slay the marauding beasts terrorizing local farmsteads.",
            "Present proof of valor to receive knighthood."
        ],
        "rewards": ["100,000 XP", "+400 Karma", "Knight's Signet Ring"]
    },
    {
        "slug": "pierscien_mieszczanina",
        "title": "The Burgher's Signet Ring",
        "region": "Noble Orders & Relics",
        "subregion": "Merchant Guild",
        "npc": "Guildmaster Jan",
        "location": "Town Square",
        "min_level": "40",
        "story": "The merchant burghers award their official signet to dependable traders and adventurers who resolve guild disputes.",
        "steps": [
            "Speak with Guildmaster Jan in the central square.",
            "Deliver trading permits and resolve supply shortages.",
            "Claim your recognized burgher status."
        ],
        "rewards": ["40,000 XP", "+200 Karma", "Burgher's Signet Ring"]
    },
    {
        "slug": "pierscien_magnata",
        "title": "The Magnate's Signet Ring",
        "region": "Noble Orders & Relics",
        "subregion": "Grand Court",
        "npc": "Grand Duke Stanislaw",
        "location": "Royal Palace",
        "min_level": "120",
        "story": "Grand Duke Stanislaw commands vast wealth and holdings. Earn his imperial favor to wear the Magnate's Signet Ring.",
        "steps": [
            "Gain audience with Grand Duke Stanislaw.",
            "Acquire rare precious gems from dragon peaks.",
            "Deliver the tribute to receive the grand title."
        ],
        "rewards": ["250,000 XP", "+800 Karma", "Magnate's Signet Ring", "50,000 Money"]
    },
    {
        "slug": "zlota_ciupaga_was",
        "title": "The Golden Axe (Single Whisk)",
        "region": "Noble Orders & Relics",
        "subregion": "Highland Forge",
        "npc": "Blacksmith Józek",
        "location": "Kościelisko Forge",
        "min_level": "70",
        "story": "Master smith Józek knows ancient techniques to forge the legendary Złota Ciupaga (Golden Axe with Whisk).",
        "steps": [
            "Bring a standard Golden Axe and silver bars to Józek.",
            "Gather rare timber from the deep forest.",
            "Assist Józek in tempering the magical blade."
        ],
        "rewards": ["80,000 XP", "Złota Ciupaga z Wąsem (Whisked Axe)", "+300 Karma"]
    },
    {
        "slug": "zlota_ciupaga_dwa_wasy",
        "title": "The Golden Axe (Double Whisk)",
        "region": "Noble Orders & Relics",
        "subregion": "Highland Forge",
        "npc": "Blacksmith Józek",
        "location": "Kościelisko Forge",
        "min_level": "110",
        "story": "The pinnacle of highland smithing: Józek can upgrade your Golden Axe with a double whisk, turning it into a devastating weapon.",
        "steps": [
            "Present your Single Whisk Golden Axe to Józek.",
            "Gather dragon blood and celestial iron.",
            "Forge the masterwork Złota Ciupaga z Dwoma Wąsami."
        ],
        "rewards": ["200,000 XP", "Złota Ciupaga z 2 Wąsami (Master Axe)", "+500 Karma"]
    },
    {
        "slug": "obsmedal",
        "title": "Obsidian Medal of Valor",
        "region": "Noble Orders & Relics",
        "subregion": "Warrior Guild",
        "npc": "Trener Wojowników",
        "location": "Warrior Guildhall",
        "min_level": "90",
        "story": "The Warrior Trainer awards the coveted Obsidian Medal to warriors who prove endurance against lethal subterranean fiends.",
        "steps": [
            "Speak with the Warrior Trainer in the Guildhall.",
            "Eliminate 50 obsidian beasts solo.",
            "Claim your Obsidian Medal of Valor."
        ],
        "rewards": ["120,000 XP", "Obsidian Medal", "+400 Karma"]
    },
    {
        "slug": "rubmedal",
        "title": "Ruby Medal of Arcana",
        "region": "Noble Orders & Relics",
        "subregion": "Mage Academy",
        "npc": "Trener Magów",
        "location": "Arcane Tower",
        "min_level": "90",
        "story": "The Mage Trainer bestows the glowing Ruby Medal upon practitioners of the arcane who master fire and mind trials.",
        "steps": [
            "Speak with the Mage Trainer at the Arcane Tower.",
            "Gather 10 pure fiery essence orbs.",
            "Channel the energy to craft the Ruby Medal."
        ],
        "rewards": ["120,000 XP", "Ruby Medal", "+400 Karma"]
    },
    {
        "slug": "sapmedal",
        "title": "Sapphire Medal of Marksmanship",
        "region": "Noble Orders & Relics",
        "subregion": "Archery Range",
        "npc": "Trener Łuczników",
        "location": "Ranger Camp",
        "min_level": "90",
        "story": "The Archer Trainer recognizes supreme precision and agility with the prestigious Sapphire Medal.",
        "steps": [
            "Demonstrate unerring archery accuracy in ranger trials.",
            "Defeat swift air creatures.",
            "Earn the Sapphire Medal of Marksmanship."
        ],
        "rewards": ["120,000 XP", "Sapphire Medal", "+400 Karma"]
    },
    # Dedicated Storage Bags (Empire of Thorns)
    {
        "slug": "the_weapon_bag",
        "title": "The Weapon Bag",
        "region": "Semos City & Environs",
        "subregion": "Semos Guardhouse",
        "npc": "Hayunn Naratha",
        "location": "Semos Guardhouse (0_semos_city)",
        "min_level": "10",
        "story": "Hayunn Naratha, the veteran adventurer in Semos Guardhouse, offers to craft a reinforced, specialized Weapon Bag for warriors who bring him the necessary materials, allowing separate organized carrying of weaponry.",
        "steps": [
            "Speak with Hayunn Naratha in the Semos Guardhouse about crafting a #weapon #bag.",
            "Gather the requested crafting materials: 1 Dagger, 2 Iron Bars, and 2 Pelts.",
            "Deliver the materials to Hayunn Naratha.",
            "Receive and unlock the dedicated Weapon Bag interface."
        ],
        "rewards": ["500 XP", "+25 Karma", "Weapon Bag (Dedicated Storage)"]
    },
    {
        "slug": "the_scroll_bag",
        "title": "The Scroll Bag",
        "region": "Ados City & Environs",
        "subregion": "Ados Magician House",
        "npc": "Haizen",
        "location": "Ados Magician House",
        "min_level": "10",
        "story": "Magician Haizen in his house outside Ados can enchant a dedicated Scroll Bag, granting spellcasters and explorers a protected pouch to safely store and organize their magic scrolls.",
        "steps": [
            "Speak with Haizen in the Ados Magician House about an enchanted #scroll #bag.",
            "Acquire 2 Blank Scrolls (from magic shops/scribes), 2 Small Feathers (dropped by fowls/birds), 2 Pelts, and 2 Silk Glands (dropped by Giant Spiders).",
            "Deliver all materials to Haizen in the Ados Magician House.",
            "Receive and unlock the dedicated Scroll Bag interface."
        ],
        "rewards": ["500 XP", "+25 Karma", "Scroll Bag (Dedicated Storage)"]
    }
]

# 3. Generate Individual HTML Guide Pages for Custom Quests
for q in custom_quests:
    steps_html = "\n".join([f"<li>{s}</li>" for s in q["steps"]])
    rewards_html = "\n".join([f"<li>{r}</li>" for r in q["rewards"]])

    # Notice: Do NOT include duplicate <h1> inside boxContent because content/wiki.php already wraps writeContent in startBox('<h1>'.$pageTitle.'</h1>')!
    guide_content = f"""<div id="mw-content-text"><div id="mw-content-text" class="mw-body-content mw-content-ltr" lang="en" dir="ltr"><div class="mw-parser-output">

<p><br /><b>Story</b><br /></p>
<blockquote><p>{q["story"]}</p></blockquote>

<p><b>Participants</b></p>
<ul>
    <li><a href="/npc/{q["slug"]}.html" class="overliblink stendhalNPCLink">{q["npc"]}</a> in <b>{q["location"]}</b> (Minimum Level: <b>{q["min_level"]}</b>).</li>
</ul>

<p><b>Steps</b></p>
<ol>
{steps_html}
</ol>

<p><b>Rewards</b></p>
<ul>
{rewards_html}
</ul>

</div></div></div>"""

    out_p = os.path.join(QUEST_DIR, f"{q['slug']}.html")
    with open(out_p, "w", encoding="utf-8") as f:
        f.write(guide_content)

print(f"Generated {len(custom_quests)} custom quest guides in {QUEST_DIR}")

# 4. Generate Master quest.html merging official categories + custom categories
official_html = ""
scratch_p = "c:/Projetos/Stendhal/EmpireOfThorns/scratch_official_quest.html"
master_p = os.path.join(WIKI_DIR, "quest.html")

if os.path.exists(scratch_p):
    with open(scratch_p, "r", encoding="utf-8") as f:
        official_html = f.read()
elif os.path.exists(master_p):
    with open(master_p, "r", encoding="utf-8") as f:
        official_html = f.read()

# Strip any previous Empire_of_Thorns_Quests headers to prevent compounding duplication
regional_idx = official_html.find('id="Regional_Quests"')
if regional_idx != -1:
    h1_idx = official_html.rfind('<h1', 0, regional_idx)
    if h1_idx != -1:
        official_html = official_html[h1_idx:]

end_pos = official_html.rfind('<!-- Saved in parser cache')
if end_pos == -1:
    end_pos = official_html.rfind('</div></div></div>')

if end_pos != -1:
    official_body = official_html[:end_pos]
else:
    official_body = official_html

custom_by_region = {}
for q in custom_quests:
    reg = q["region"]
    sub = q["subregion"]
    if reg not in custom_by_region:
        custom_by_region[reg] = {}
    if sub not in custom_by_region[reg]:
        custom_by_region[reg][sub] = []
    custom_by_region[reg][sub].append(q)

custom_sections = []
for reg, subs in custom_by_region.items():
    sub_blocks = []
    for sub, qlist in subs.items():
        items = "\n".join([f'<li><a href="/quest/{q["slug"]}.html" title="{q["title"]}">{q["title"]}</a> (Min Level {q["min_level"]})</li>' for q in qlist])
        sub_blocks.append(f"<h3><span class=\"mw-headline\">{sub}</span></h3>\n<ul>\n{items}\n</ul>")
    
    custom_sections.append(f"""<hr />
<h2><span class=\"mw-headline\">{reg}</span></h2>
{"".join(sub_blocks)}""")

merged_custom_html = "\n".join(custom_sections)

full_master_quest_html = f"""<div id="mw-content-text"><div id="mw-content-text" class="mw-body-content mw-content-ltr" lang="en" dir="ltr"><div class="mw-parser-output">
<p>This page shows quests that exist in Stendhal and the Empire of Thorns expansion. Find out what stage you are at in any quest by checking your in-game quest log.</p>

<h1><span class="mw-headline" id="Empire_of_Thorns_Quests">Empire of Thorns Quests</span></h1>
{merged_custom_html}

<hr />
{official_body}

</div></div></div>"""

master_p = os.path.join(WIKI_DIR, "quest.html")
with open(master_p, "w", encoding="utf-8") as f:
    f.write(full_master_quest_html)

print("Generated beautiful merged master quest.html!")
