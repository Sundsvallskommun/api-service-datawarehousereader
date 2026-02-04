drop table if exists kundinfo.vCustomer;
drop table if exists kundinfo.vCustomerDetail;
drop table if exists kundinfo.vInstalledBaseMetadata;
drop table if exists kundinfo.vInstalledBase;
drop table if exists kundinfo.vInstallations;
drop table if exists kundinfo.vInvoiceDetail_Test_251126;
drop table if exists kundinfo.vInvoice_Test_251126;
drop table if exists kundinfo.vAgreements;
drop procedure if exists kundinfo.spMeasurementDistrictHeating;
drop procedure if exists kundinfo.spMeasurementElectricity;
drop function if exists kundinfo.fnCustomerDetailWithPagingAndSort;

drop schema if exists kundinfo;
create schema kundinfo;

create procedure kundinfo.spMeasurementDistrictHeating(@customerorgid as varchar(8000),
                                                       @anlaggningsID as varchar(255), @datum_start as datetime2,
                                                       @datum_stop as datetime2, @aggregationLevel as varchar(50))
as
begin
    -- Test case: HOUR aggregation for legalId 5591561234 and facilityId 9115803075
    -- Production does NOT return interpolation column for HOUR - mapper defaults to 0
    if @customerorgid = '5591561234' and @anlaggningsID = '9115803075' and @aggregationLevel = 'HOUR'
    begin
        select 'A0B52C5B-93AC-480B-821D-E238C8F4D952' as uuid, '5591561234' as customerorgid, '9115803075' as facilityId, 'Aktiv' as feedType, 'kWh' as unit, cast(7910.04 as decimal(28,10)) as [usage], cast('2022-01-01 01:00:00' as datetime) as DateAndTime
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7910.18 as decimal(28,10)), cast('2022-01-01 02:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7910.33 as decimal(28,10)), cast('2022-01-01 03:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7910.47 as decimal(28,10)), cast('2022-01-01 04:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7910.61 as decimal(28,10)), cast('2022-01-01 05:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7910.75 as decimal(28,10)), cast('2022-01-01 06:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7910.89 as decimal(28,10)), cast('2022-01-01 07:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7911.03 as decimal(28,10)), cast('2022-01-01 08:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7911.17 as decimal(28,10)), cast('2022-01-01 09:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7911.31 as decimal(28,10)), cast('2022-01-01 10:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7911.45 as decimal(28,10)), cast('2022-01-01 11:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7911.60 as decimal(28,10)), cast('2022-01-01 12:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7911.75 as decimal(28,10)), cast('2022-01-01 13:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7911.91 as decimal(28,10)), cast('2022-01-01 14:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7912.06 as decimal(28,10)), cast('2022-01-01 15:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7912.23 as decimal(28,10)), cast('2022-01-01 16:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7912.39 as decimal(28,10)), cast('2022-01-01 17:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7912.54 as decimal(28,10)), cast('2022-01-01 18:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7912.71 as decimal(28,10)), cast('2022-01-01 19:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7912.89 as decimal(28,10)), cast('2022-01-01 20:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7913.06 as decimal(28,10)), cast('2022-01-01 21:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7913.23 as decimal(28,10)), cast('2022-01-01 22:00:00' as datetime)
        union all select 'A0B52C5B-93AC-480B-821D-E238C8F4D952', '5591561234', '9115803075', 'Aktiv', 'kWh', cast(7913.38 as decimal(28,10)), cast('2022-01-01 23:00:00' as datetime)
    end

    -- Test case: DAY aggregation for legalId 5566661234 and facilityId 9261219043
    -- Production uses column name 'isInterpolted' (typo) for DAY
    else if @customerorgid = '5566661234' and @anlaggningsID = '9261219043' and @aggregationLevel = 'DAY'
    begin
        select null as uuid, '5566661234' as customerorgid, '9261219043' as facilityId, 'energy' as feedType, 'kWh' as unit, cast(1393 as decimal(28,10)) as [usage], cast('2022-03-23 00:00:00' as datetime) as DateAndTime, 0 as isInterpolted
        union all select null, '5566661234', '9261219043', 'energy', 'kWh', cast(1403 as decimal(28,10)), cast('2022-03-24 00:00:00' as datetime), 0
        union all select null, '5566661234', '9261219043', 'energy', 'kWh', cast(1567 as decimal(28,10)), cast('2022-03-25 00:00:00' as datetime), 0
    end

    -- Test case: MONTH aggregation for legalId 5534567890 and facilityId 735999109113202014
    -- Production uses column name 'isInterpolated' (correct spelling) for MONTH
    else if @customerorgid = '5534567890' and @anlaggningsID = '735999109113202014' and @aggregationLevel = 'MONTH'
    begin
        select null as uuid, '5534567890' as customerorgid, '735999109113202014' as facilityId, 'Aktiv' as feedType, 'kWh' as unit, cast(1772.10 as decimal(28,10)) as [usage], cast('2018-02-01 00:00:00' as datetime) as DateAndTime, 0 as isInterpolated
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(2060.90 as decimal(28,10)), cast('2018-03-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(1961.68 as decimal(28,10)), cast('2018-04-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(1896.27 as decimal(28,10)), cast('2018-05-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(1691.52 as decimal(28,10)), cast('2018-06-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(1752.58 as decimal(28,10)), cast('2018-07-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(1884.22 as decimal(28,10)), cast('2018-08-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(1781.29 as decimal(28,10)), cast('2018-09-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(1853.54 as decimal(28,10)), cast('2018-10-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(2116.19 as decimal(28,10)), cast('2018-11-01 00:00:00' as datetime), 0
        union all select null, '5534567890', '735999109113202014', 'Aktiv', 'kWh', cast(2111.47 as decimal(28,10)), cast('2018-12-01 00:00:00' as datetime), 0
    end

    -- Default: return empty result set
    else
    begin
        select cast(null as varchar(36)) as uuid,
               cast(null as varchar(8000)) as customerorgid,
               cast(null as varchar(255)) as facilityId,
               cast(null as varchar(255)) as feedType,
               cast(null as varchar(255)) as unit,
               cast(null as decimal(28,10)) as [usage],
               cast(null as datetime) as DateAndTime
        where 1 = 0
    end
end;

create procedure kundinfo.spMeasurementElectricity(
        @customerorgid varchar(8000),
        @anlaggningsID varchar(255),
        @datum_start datetime2,
        @datum_stop datetime2,
        @aggregationLevel varchar(50)
    )
    as
    begin
        -- Test case: DAY aggregation for legalId 5534567890 and facilityId 735999109170208042
        -- Production uses column name 'isInterpolted' (typo) for DAY
        if @customerorgid = '5534567890' and @anlaggningsID = '735999109170208042' and @aggregationLevel = 'DAY'
        begin
            select '16A64870-DF4D-4A27-A514-56297AB6F8D9' as uuid,
                   '5534567890' as customerorgid,
                   '735999109170208042' as facilityId,
                   'Energy' as feedType,
                   'kWh' as unit,
                   cast(4.587 as decimal(28,10)) as [usage],
               cast('2022-04-02 00:00:00' as datetime) as DateAndTime,
               24 as isInterpolted
            union all
            select '16A64870-DF4D-4A27-A514-56297AB6F8D9', '5534567890', '735999109170208042', 'Energy', 'kWh',
                   cast(5.879 as decimal(28,10)), cast('2022-04-03 00:00:00' as datetime), 23
            union all
            select '16A64870-DF4D-4A27-A514-56297AB6F8D9', '5534567890', '735999109170208042', 'Energy', 'kWh',
                   cast(6.41 as decimal(28,10)), cast('2022-04-08 00:00:00' as datetime), 0
            union all
            select '16A64870-DF4D-4A27-A514-56297AB6F8D9', '5534567890', '735999109170208042', 'Energy', 'kWh',
                   cast(4.587 as decimal(28,10)), cast('2022-04-09 00:00:00' as datetime), 24
            union all
            select '16A64870-DF4D-4A27-A514-56297AB6F8D9', '5534567890', '735999109170208042', 'Energy', 'kWh',
                   cast(5.492 as decimal(28,10)), cast('2022-04-10 00:00:00' as datetime), 23
                       end

            -- Test case: HOUR aggregation for legalId 195211161234 and facilityId 735999109320425015
            -- Production does NOT return interpolation column for HOUR - mapper defaults to 0
            else if @customerorgid = '195211161234' and @anlaggningsID = '735999109320425015' and @aggregationLevel = 'HOUR'
            begin
                select '62743983-9C08-4CB4-A7F6-1DAAE3889733' as uuid, '195211161234' as customerorgid, '735999109320425015' as facilityId, 'Energy' as feedType, 'kWh' as unit, cast(0.06 as decimal(28,10)) as [usage], cast('2022-04-11 00:00:00' as datetime) as DateAndTime
                union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.02 as decimal(28,10)), cast('2022-04-11 01:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.07 as decimal(28,10)), cast('2022-04-11 02:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.01 as decimal(28,10)), cast('2022-04-11 03:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.03 as decimal(28,10)), cast('2022-04-11 04:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.04 as decimal(28,10)), cast('2022-04-11 05:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.04 as decimal(28,10)), cast('2022-04-11 06:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.06 as decimal(28,10)), cast('2022-04-11 07:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.01 as decimal(28,10)), cast('2022-04-11 08:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.02 as decimal(28,10)), cast('2022-04-11 09:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.03 as decimal(28,10)), cast('2022-04-11 10:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.02 as decimal(28,10)), cast('2022-04-11 11:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.02 as decimal(28,10)), cast('2022-04-11 12:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.02 as decimal(28,10)), cast('2022-04-11 13:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.02 as decimal(28,10)), cast('2022-04-11 14:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.04 as decimal(28,10)), cast('2022-04-11 15:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.02 as decimal(28,10)), cast('2022-04-11 16:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.03 as decimal(28,10)), cast('2022-04-11 17:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.02 as decimal(28,10)), cast('2022-04-11 18:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.06 as decimal(28,10)), cast('2022-04-11 19:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.01 as decimal(28,10)), cast('2022-04-11 20:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.03 as decimal(28,10)), cast('2022-04-11 21:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.05 as decimal(28,10)), cast('2022-04-11 22:00:00' as datetime)
                          union all select '62743983-9C08-4CB4-A7F6-1DAAE3889733', '195211161234', '735999109320425015', 'Energy', 'kWh', cast(0.04 as decimal(28,10)), cast('2022-04-11 23:00:00' as datetime)
                    end

                -- Test case: MONTH aggregation for legalId 197706010123 and facilityId 9151530012
                -- Production uses column name 'isInterpolated' (correct spelling) for MONTH
                else if @customerorgid = '197706010123' and @anlaggningsID = '9151530012' and @aggregationLevel = 'MONTH'
                begin
                    select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B' as uuid, '197706010123' as customerorgid, '9151530012' as facilityId, 'Energy' as feedType, 'C' as unit, cast(0 as decimal(28,10)) as [usage], cast('2019-02-01 00:00:00' as datetime) as DateAndTime, 0 as isInterpolated
                    union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'kW', cast(0 as decimal(28,10)), cast('2019-02-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3', cast(63.81 as decimal(28,10)), cast('2019-02-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3/h', cast(0 as decimal(28,10)), cast('2019-02-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'MWh', cast(2.065 as decimal(28,10)), cast('2019-02-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'C', cast(0 as decimal(28,10)), cast('2019-03-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'kW', cast(0 as decimal(28,10)), cast('2019-03-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3', cast(68.13 as decimal(28,10)), cast('2019-03-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3/h', cast(0 as decimal(28,10)), cast('2019-03-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'MWh', cast(2.147 as decimal(28,10)), cast('2019-03-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'C', cast(0 as decimal(28,10)), cast('2019-04-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'kW', cast(0 as decimal(28,10)), cast('2019-04-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3', cast(46.41 as decimal(28,10)), cast('2019-04-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3/h', cast(0 as decimal(28,10)), cast('2019-04-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'MWh', cast(1.418 as decimal(28,10)), cast('2019-04-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'C', cast(0 as decimal(28,10)), cast('2019-05-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'kW', cast(0 as decimal(28,10)), cast('2019-05-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3', cast(33.82 as decimal(28,10)), cast('2019-05-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3/h', cast(0 as decimal(28,10)), cast('2019-05-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'MWh', cast(1.093 as decimal(28,10)), cast('2019-05-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'C', cast(0 as decimal(28,10)), cast('2019-06-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'kW', cast(0 as decimal(28,10)), cast('2019-06-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3', cast(10.06 as decimal(28,10)), cast('2019-06-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'm3/h', cast(0 as decimal(28,10)), cast('2019-06-01 00:00:00' as datetime), 0
                              union all select 'B252BC0D-AC49-46A2-B20B-B63DFE9C812B', '197706010123', '9151530012', 'Energy', 'MWh', cast(0.306 as decimal(28,10)), cast('2019-06-01 00:00:00' as datetime), 0
                        end

                    -- Test case: QUARTER aggregation for legalId 198001011234 and facilityId 735999109440512001
                    -- Production uses column name 'isInterpolted' (typo) for QUARTER
                    else if @customerorgid = '198001011234' and @anlaggningsID = '735999109440512001' and @aggregationLevel = 'QUARTER'
                    begin
                        select 'A7B8C9D0-E1F2-3456-7890-ABCDEF123456' as uuid,
                               '198001011234' as customerorgid,
                               '735999109440512001' as facilityId,
                               'Energy' as feedType,
                               'kWh' as unit,
                               cast(125.5 as decimal(28,10)) as [usage],
                               cast('2022-01-01 00:00:00' as datetime) as DateAndTime,
                               5 as isInterpolted
                        union all
                        select 'A7B8C9D0-E1F2-3456-7890-ABCDEF123456', '198001011234', '735999109440512001', 'Energy', 'kWh',
                               cast(142.3 as decimal(28,10)), cast('2022-02-01 00:00:00' as datetime), 3
                        union all
                        select 'A7B8C9D0-E1F2-3456-7890-ABCDEF123456', '198001011234', '735999109440512001', 'Energy', 'kWh',
                               cast(118.7 as decimal(28,10)), cast('2022-03-01 00:00:00' as datetime), 2
                    end

                    -- Default: return empty result set
                    else
                    begin
                        select cast(null as varchar(36)) as uuid,
                               cast(null as varchar(8000)) as customerorgid,
                               cast(null as varchar(255)) as facilityId,
                               cast(null as varchar(255)) as feedType,
                               cast(null as varchar(255)) as unit,
                               cast(null as decimal(28,10)) as [usage],
               cast(null as datetime) as DateAndTime
                        where 1 = 0
                    end
                    end;
