drop table if exists kundinfo.vCustomer;
drop table if exists kundinfo.vCustomerDetail;
drop table if exists kundinfo.vInstalledBaseMetadata;
drop table if exists kundinfo.vInstalledBase;
drop table if exists kundinfo.vInvoiceDetail;
drop table if exists kundinfo.vInvoice;
drop table if exists kundinfo.vMeasurementDistrictHeatingHour;
drop table if exists kundinfo.vMeasurementDistrictHeatingDay;
drop table if exists kundinfo.vMeasurementDistrictHeatingMonth;
drop table if exists kundinfo.vMeasurementElectricityHour;
drop table if exists kundinfo.vMeasurementElectricityDay;
drop table if exists kundinfo.vMeasurementElectricityMonth;
drop table if exists kundinfo.vAgreements;
drop procedure if exists kundinfo.spMeasurementElectricityHour;
drop procedure if exists kundinfo.spCustomerDetails;
drop function if exists kundinfo.fnCustomerDetails;


drop schema if exists kundinfo;
create schema kundinfo;

create procedure kundinfo.spMeasurementElectricityHour(@customerorgid as varchar(8000), @anlaggningsID as varchar(255), @datum_start as datetime2, @datum_stop as datetime2)
	as
begin
    select uuid,
           customerorgid,
           facilityId,
           feedType,
           unit, [usage], DateAndTime
    from vMeasurementElectricityHour
    where (@customerorgid is null
       or @customerorgid = customerorgid)
      and (@anlaggningsID is null
       or @anlaggningsID = facilityId)
      and (@datum_start is null
       or @datum_start <= DateAndTime)
      and (@datum_stop is null
       or @datum_stop >= DateAndTime)
end;


CREATE PROCEDURE kundinfo.spCustomerDetails @fromDate datetime2, @pageOffset int, @pageSize int
AS
begin

    SELECT DISTINCT Uuid,
                    CustomerOrgId,
                    CustomerId,
                    CustomerCategoryID,
                    CustomerCategoryDescription,
                    Name,
                    [c/o],
                    Address,
                    Zipcode,
                    City,
                    Phone1,
                    Phone2,
                    Phone3,
                    Email1,
                    Email2,
                    CustomerChangedFlg,
                    Installedchangedflg
    from vCustomerDetail
    order by CustomerId
    OFFSET @pageOffset ROWS FETCH NEXT @pageSize ROWS ONLY
end;

CREATE FUNCTION kundinfo.fnCustomerDetails(@fromDate datetime2)
    RETURNS @table TABLE (CustomerCategoryID int,
                          CustomerChangedFlg bit,
                          Customerid int not null,
                          Installedchangedflg bit,
                          Address nvarchar(255),
                          [C/o] nvarchar(255),
                          City nvarchar(255),
                          CustomerCategoryDescription nvarchar(255),
                          CustomerOrgId nvarchar(255),
                          Email1 nvarchar(255),
                          Email2 nvarchar(255),
                          Name nvarchar(255),
                          Phone1 nvarchar(255),
                          Phone2 nvarchar(255),
                          Phone3 nvarchar(255),
                          uuid uniqueidentifier,
                          Zipcode nvarchar(255))
AS BEGIN
    INSERT @table
    (uuid, customerId, customerOrgId, customerCategoryID, customerCategoryDescription, name, "c/o", address, zipcode, city, phone1, phone2, phone3, email1, email2, customerChangedFlg, installedChangedFlg) VALUES
    ('9f395f51-b5ed-401b-b700-ef70cbb15d79', 123454,'102000-0000',2,N'Företag1','Test Testorsson','c/o Testorsson',N'Testvägen12 c lgh 1005',85234,'Sundsvall','+46761234567',NULL,'+46761234567',NULL,NULL,0,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d80', 123455,'102000-0000',2,N'Företag2','Test Testorsson','c/o Testorsson',N'Testvägen11 c lgh 1005',85234,'Sundsvall','+46761234567',NULL,'+46761234567',NULL,NULL,0,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d81', 123456,'102000-0000',2,N'Företag','Test Testorsson','c/o Testorsson',N'Testvägen10 c lgh 1005',85234,'Sundsvall','+46761234567',NULL,'+46761234567',NULL,NULL,0,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d82', 123457,'20000101-1234',1,'Privatperson','Test Testorsson',NULL,N'Testvägen21',85234,'Sundsvall','+46761234567','+46761234567',NULL,'test@sundsvall.com','test2@sundsvall.com',0,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d83', 123458,'20010101-1234',1,'Privatperson','Test Testorsson',NULL,N'Testvägen2',85234,'Sundsvall','+46761234567',NULL,NULL,'test@sundsvall.com','test2@sundsvall.com',1,0),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d84', 123459,'20020101-1234',1,'Privatperson','Test Testorsson',NULL,N'Testvägen21 E',85234,'Sundsvall','+46761234567',NULL,NULL,NULL,'test2@sundsvall.com',0,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d85', 123450,'20030101-1234',1,'Privatperson','Test Testorsson',NULL,N'Testvägen65 A',85234,'Sundsvall','+46761234567',NULL,NULL,NULL,'test2@sundsvall.com',1,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d86', 123410,'20040101-1234',1,'Privatperson','Test Testorsson',NULL,N'Testvägen60',85234,'Sundsvall','+46761234567',NULL,NULL,NULL,NULL,0,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d87', 123411,'20050101-1234',1,'Privatperson','Test Testorsson',NULL,N'Testvägen12',85234,'SUNDSVALL','+46761234567',NULL,NULL,'test@sundsvall.com',NULL,0,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d88', 123412,'20060101-1234',1,'Privatperson','Test Testorsson',NULL,N'Testvägen2 Lgh 6',85234,'Sundsvall',NULL,'+46761234567',NULL,NULL,NULL,1,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d89', 123413,'20070101-1234',1,'Privatperson','Test Testorsson2',NULL,N'Testvägen106',85234,'Sundsvall',NULL,'+46761234567',NULL,NULL,'test2@sundsvall.com',0,1),
    ('9f395f51-b5ed-401b-b700-ef70cbb15d90', 123414,'20070101-1234',1,'Privatperson','Test Testorsson3',NULL,N'Testvägen106',85234,'Sundsvall',NULL,'+46761234567',NULL,NULL,'test3@sundsvall.com',0,1),
    (null,                                   123415,'20070101-1234',1,'Privatperson','Test Testorsson4',NULL,N'Testvägen106',85234,'Sundsvall',NULL,'+46761234567',NULL,NULL,'test4@sundsvall.com',0,1)
    RETURN
END