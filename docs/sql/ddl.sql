CREATE TABLE IF NOT EXISTS `Score`
(
    `game_id`     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `timestamp`   INTEGER                           NOT NULL,
    `guess_count` INTEGER                           NOT NULL,
    `code_length` INTEGER                           NOT NULL
);

CREATE INDEX IF NOT EXISTS `index_Score_timestamp` ON `Score` (`timestamp`);

CREATE INDEX IF NOT EXISTS `index_Score_code_length` ON `Score` (`code_length`);
