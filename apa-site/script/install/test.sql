TRUNCATE `ld_raw_domain`;
TRUNCATE `sd_suspect_domain`;
TRUNCATE `su_clue_url`;
TRUNCATE `su_suspect_url`;

select 'ld_raw_domain' as data_label, count(*) as data_count from ld_raw_domain union
select 'sd_suspect_domain' as data_label, count(*) as data_count from sd_suspect_domain union
select 'su_clue_url' as data_label, count(*) as data_count from su_clue_url union
select 'su_suspect_url' as data_label, count(*) as data_count from su_suspect_url


select progress,count(*) from su_clue_url group by progress;
select url,create_time from su_suspect_url order by create_time ASC;

insert into ld_raw_domain_201801 (select * from ld_raw_domain as ld where ld.create_time >= '2018-01-01 00:00:00' and ld.create_time < '2018-02-01 00:00:00')
delete from ld_raw_domain where create_time >= '2018-01-01 00:00:00' and create_time < '2018-02-01 00:00:00'