-- こんなかんじ
-- のコメントあっても問題なし
--   
--   
-- 

/*
 
コメント1
 
*/

/*
 
*コメント2
 
*/


CREATE TABLE todo (
--先頭にtab、間もtab
	id		integer	not null	,
--先頭に空白スペース、間も空白スペース
    title   character varying (32)   ,
--content character varyingと(の間にスペースなし
content character varying(100),
--型が複数トークン
limit_time timestamp with time zone,
regist_date date
);

--主キー
ALTER TABLE todo ADD CONSTRAINT pk_todo
    PRIMARY KEY( 
    id
);

--コメント
COMMENT ON INDEX pk_todo IS 'todo主キー';


