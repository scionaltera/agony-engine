ALTER TABLE body_part ADD COLUMN connection_id UUID;
ALTER TABLE item_info ADD COLUMN use_all_slots BOOLEAN NOT NULL DEFAULT FALSE;

-- Because we changed the enum for WearLocation it'll be safest to remove any items people
-- have worn and reset all items to not be wearable.

UPDATE actor SET game_map_id='5231e20f-0658-4685-9396-6e69ebfb2c3b', x=0, y=0
FROM item_info WHERE item_info.id=actor.item_info_id
AND item_info.wear_locations > 0;

UPDATE body_part SET armor_id=NULL;
UPDATE item_info SET wear_locations=0;
