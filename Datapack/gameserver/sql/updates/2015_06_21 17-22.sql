ALTER TABLE character_hennas ADD COLUMN `is_premium` TINYINT UNSIGNED NOT NULL DEFAULT '0' AFTER `draw_time`;