create table kundinfo.vAgreements
(
    agreementId   int        not null,
    billingId     int        not null,
    customerid    int        not null,
    fromDate      datetime,
    toDate        datetime,
    binding       varchar(5) not null,
    bindingRule   nvarchar(255),
    category      nvarchar(255),
    customerorgid varchar(8000),
    description   varchar(4096),
    facilityId    varchar(255),
    mainAgreement varchar(5) not null,
    uuid uniqueidentifier,
    primary key (agreementId, billingId)
);

create table kundinfo.vCustomer
(
    customerid       int         not null,
    customerorgid    varchar(255),
    customertype     varchar(10) not null,
    organizationid   varchar(10) not null,
    organizationname nvarchar(255),
    primary key (customerid, organizationid)
);

create table kundinfo.vCustomerDetail
(
    CustomerCategoryID          int,
    CustomerChangedFlg          bit,
    Customerid                  int not null,
    Installedchangedflg         bit,
    Address                     nvarchar(255),
    City                        nvarchar(255),
    CustomerCategoryDescription nvarchar(255),
    CustomerOrgId               nvarchar(255),
    Email1                      nvarchar(255),
    Email2                      nvarchar(255),
    Name                        nvarchar(255),
    Phone1                      nvarchar(255),
    Phone2                      nvarchar(255),
    Phone3                      nvarchar(255),
    Zipcode                     nvarchar(255), [
    c
    /
    o]
    nvarchar
(
    255
), organizationid nvarchar(255), organizationname nvarchar(255), uuid uniqueidentifier, primary key (Customerid) );

create table kundinfo.vInstallations
(
    BillLocationID               int not null,
    CustomerFlg                  bit not null,
    DateFrom                     datetime,
    DateTo                       datetime,
    InstallationsLastChangedDate datetime,
    InternalId                   int not null,
    Careof                       varchar(255),
    City                         varchar(255),
    Company                      nvarchar(255),
    FacilityId                   varchar(255),
    HouseName                    varchar(255),
    Postcode                     varchar(255),
    Street                       varchar(255),
    Type                         nvarchar(255),
    primary key (BillLocationID)
);

create table kundinfo.vInstalledBase
(
    BillLocationID               int not null,
    DateFrom                     datetime,
    DateTo                       datetime,
    InstalledBaseLastChangedDate datetime,
    InternalId                   int not null,
    customerid                   int not null,
    CareOf                       varchar(255),
    City                         varchar(255),
    Company                      nvarchar(255),
    FacilityId                   varchar(255),
    HouseName                    varchar(255),
    Postcode                     varchar(255),
    Street                       varchar(255),
    Type                         nvarchar(255),
    primary key (BillLocationID)
);

create table kundinfo.vInstalledBaseMetadata
(
    internalId  int         not null,
    company     nvarchar(255),
    displayName varchar(26) not null, [
    key]
    varchar
(
    255
) not null, [type] varchar(8) not null, [value] nvarchar(255)
    );

create table kundinfo.vInvoice
(
    AmountVatExcluded money,
    AmountVatIncluded money,
    CustomerId         int         not null,
    DueDate            date,
    InvoiceDate        date,
    PdfAvailable       varchar(5)  not null,
    ReversedVat        varchar(5)  not null,
    Rounding money,
    TotalAmount money,
    Vat money,
    VatEligiblaAmount money,
    InvoiceNumber      bigint      not null,
    OcrNumber          bigint,
    Administration     nvarchar(255),
    CareOf             varchar(255),
    City               varchar(255),
    Currency           varchar(3)  not null,
    CustomerType       varchar(10),
    FacilityId         varchar(255),
    InvoiceDescription nvarchar(255),
    InvoiceName        varchar(28) not null,
    InvoiceStatus      nvarchar(255),
    InvoiceType        nvarchar(255),
    OrganizationGroup  varchar(11) not null,
    OrganizationId     varchar(10),
    PostalCode         varchar(255),
    Street             varchar(255),
    primary key (InvoiceNumber)
);

create table kundinfo.vInvoiceDetail
(
    Amount money,
    AmountVatExcluded money,
    Productcode       smallint not null,
    Quantity          float(53),
    Unitprice money,
    Vat money,
    Vatrate           float(53),
    invoiceId         int      not null,
    invoiceProductSeq int      not null,
    InvoiceNumber     bigint,
    Description       nvarchar(255),
    OrganizationId    varchar(10),
    ProductName       nvarchar(255),
    periodFrom        nvarchar(4000),
    periodTo          nvarchar(4000),
    unit              nvarchar(255),
    primary key (invoiceProductSeq)
);

create table kundinfo.vMeasurementDistrictHeatingDay
(
    READING_DAY_SEQ int           not null,
    isInterpolated  tinyint       not null,
    usage           decimal(33, 10),
    DateAndTime     datetime,
    customerorgid   varchar(8000) not null,
    facilityId      varchar(255)  not null,
    feedType        nvarchar(255),
    unit            nvarchar(255),
    uuid uniqueidentifier,
    primary key (READING_DAY_SEQ, customerorgid, facilityId)
);

create table kundinfo.vMeasurementDistrictHeatingHour
(
    READING_DAY_SEQ int           not null,
    isInterpolted   bit           not null,
    usage           decimal(28, 10),
    DateAndTime     datetime,
    ReadingDetailID bigint        not null,
    customerorgid   varchar(8000) not null,
    facilityId      varchar(255),
    feedType        nvarchar(255),
    unit            nvarchar(255),
    uuid uniqueidentifier,
    primary key (ReadingDetailID)
);

create table kundinfo.vMeasurementDistrictHeatingMonth
(
    READING_SEQ    int           not null,
    isInterpolated smallint      not null,
    usage          decimal(33, 10),
    DateAndTime    datetime,
    customerorgid  varchar(8000) not null,
    facilityId     varchar(255)  not null,
    feedType       nvarchar(255),
    unit           nvarchar(255),
    uuid uniqueidentifier,
    primary key (READING_SEQ, customerorgid, facilityId)
);

create table kundinfo.vMeasurementElectricityDay
(
    READING_DAY_SEQ int             not null,
    isInterpolted   tinyint         not null,
    usage           decimal(28, 10) not null,
    DateAndTime     datetime        not null,
    customerorgid   varchar(8000)   not null,
    facilityId      varchar(255)    not null,
    feedType        varchar(6)      not null,
    unit            nvarchar(255)   not null,
    uuid uniqueidentifier,
    primary key (isInterpolted, usage, DateAndTime, customerorgid, facilityId, feedType, unit)
);

create table kundinfo.vMeasurementElectricityHour
(
    usage         decimal(28, 10) not null,
    DateAndTime   datetime        not null,
    customerorgid varchar(8000)   not null,
    facilityId    varchar(255)    not null,
    feedType      varchar(6)      not null,
    unit          nvarchar(255)   not null,
    uuid uniqueidentifier,
    primary key (usage, DateAndTime, customerorgid, facilityId, feedType, unit)
);

create table kundinfo.vMeasurementElectricityMonth
(
    Usage         decimal(28, 10) not null,
    isInterpolted smallint        not null,
    DateFrom      datetime        not null,
    customerorgid varchar(8000)   not null,
    facilityId    varchar(255)    not null,
    feedType      varchar(6)      not null,
    unit          nvarchar(255)   not null,
    uuid uniqueidentifier,
    primary key (Usage, isInterpolted, DateFrom, customerorgid, facilityId, feedType, unit)
);
