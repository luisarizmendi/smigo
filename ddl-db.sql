CREATE TABLE COMMENTS
(
  ID                INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  TEXT              VARCHAR(2147483647)                NOT NULL,
  SUBMITTER_USER_ID INTEGER                            NOT NULL,
  RECEIVER_USER_ID  INTEGER                            NOT NULL,
  YEAR              INTEGER                            NOT NULL,
  UNREAD            BOOLEAN DEFAULT TRUE               NOT NULL,
  CREATEDATE        TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);
CREATE TABLE FAMILIES
(
  ID   INTEGER PRIMARY KEY NOT NULL,
  NAME VARCHAR(2147483647) NOT NULL
);
CREATE TABLE IMPACTS
(
  ID   INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME VARCHAR(2147483647) DEFAULT 'NULL'
);
CREATE TABLE MESSAGES
(
  ID                INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  TEXT              VARCHAR(2147483647)                NOT NULL,
  SUBMITTER_USER_ID INTEGER                            NOT NULL,
  LOCATION          VARCHAR(2147483647) DEFAULT 'NULL',
  VISIBLE           BOOLEAN             DEFAULT FALSE,
  CREATEDATE        TIMESTAMP           DEFAULT CURRENT_TIMESTAMP(),
  LOCALE            VARCHAR(2147483647) DEFAULT 'en'   NOT NULL
);
CREATE TABLE OPENID
(
  ID           INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  IDENTITY_URL VARCHAR(2147483647)                NOT NULL,
  USER_ID      INTEGER                            NOT NULL
);
CREATE TABLE PERSISTENT_LOGINS
(
  USERNAME  VARCHAR(2147483647)                   NOT NULL,
  SERIES    VARCHAR(2147483647) PRIMARY KEY       NOT NULL,
  TOKEN     VARCHAR(2147483647)                   NOT NULL,
  LAST_USED TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL
);
CREATE TABLE PLANTS
(
  ID         INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  X          INTEGER                            NOT NULL,
  Y          INTEGER                            NOT NULL,
  SPECIES_ID INTEGER                            NOT NULL,
  YEAR       INTEGER                            NOT NULL,
  USER_ID    INTEGER                            NOT NULL,
  VARIETY_ID INTEGER,
  CREATEDATE TIMESTAMP DEFAULT NOW()
);
CREATE TABLE RULES
(
  ID               INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  HOST             INTEGER                            NOT NULL,
  CAUSER           INTEGER DEFAULT NULL,
  GAP              INTEGER DEFAULT NULL,
  CREATOR          INTEGER DEFAULT NULL,
  CAUSERFAMILY     INTEGER DEFAULT NULL,
  DISPLAYBYDEFAULT BOOLEAN DEFAULT TRUE,
  PUBLIC           BOOLEAN DEFAULT FALSE,
  TYPE             INTEGER DEFAULT '0'                NOT NULL
);
CREATE TABLE RULES_X_IMPACTS
(
  RULE_ID   INTEGER NOT NULL,
  IMPACT_ID INTEGER NOT NULL
);
CREATE TABLE SPECIES
(
  ID              INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  SCIENTIFIC_NAME VARCHAR(2147483647) DEFAULT 'NULL',
  ITEM            BOOLEAN             DEFAULT FALSE,
  ANNUAL          BOOLEAN             DEFAULT TRUE,
  FAMILY_ID       INTEGER             DEFAULT NULL,
  CREATOR         INTEGER             DEFAULT NULL,
  ICONFILENAME    VARCHAR(2147483647) DEFAULT 'NULL',
  CREATEDATE      TIMESTAMP           DEFAULT NOW()
);
CREATE TABLE USERCONNECTION
(
  USERID         VARCHAR(2147483647)            NOT NULL,
  PROVIDERID     VARCHAR(2147483647)            NOT NULL,
  PROVIDERUSERID VARCHAR(2147483647) DEFAULT '' NOT NULL,
  RANK           INTEGER                        NOT NULL,
  DISPLAYNAME    VARCHAR(2147483647) DEFAULT 'NULL',
  PROFILEURL     VARCHAR(2147483647) DEFAULT 'NULL',
  IMAGEURL       VARCHAR(2147483647) DEFAULT 'NULL',
  ACCESSTOKEN    VARCHAR(2147483647)            NOT NULL,
  SECRET         VARCHAR(2147483647) DEFAULT 'NULL',
  REFRESHTOKEN   VARCHAR(2147483647) DEFAULT 'NULL',
  EXPIRETIME     BIGINT              DEFAULT NULL,
  PRIMARY KEY (USERID, PROVIDERID, PROVIDERUSERID)
);
CREATE TABLE USERS
(
  ID             INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  USERNAME       VARCHAR(2147483647)                NOT NULL,
  LOCALE         VARCHAR(2147483647) DEFAULT 'NULL',
  CREATEDATE     TIMESTAMP           DEFAULT NOW(),
  TERMSOFSERVICE BOOLEAN DEFAULT FALSE              NOT NULL,
  EMAIL          VARCHAR(2147483647) DEFAULT 'NULL',
  DISPLAYNAME    VARCHAR(2147483647) DEFAULT 'NULL',
  DECIDETIME     INTEGER             DEFAULT NULL,
  ABOUT          VARCHAR(2147483647) DEFAULT 'NULL',
  PASSWORD       VARCHAR(2147483647) DEFAULT 'NULL',
  ENABLED        BOOLEAN DEFAULT TRUE               NOT NULL,
  AUTHORITY      VARCHAR(2147483647) DEFAULT 'user' NOT NULL
);
CREATE TABLE VARIETIES
(
  ID         INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  NAME       VARCHAR(2147483647)                NOT NULL,
  USER_ID    INTEGER                            NOT NULL,
  SPECIES_ID INTEGER                            NOT NULL,
  CREATEDATE TIMESTAMP DEFAULT NOW()            NOT NULL
);
CREATE TABLE VERNACULARS
(
  ID              INTEGER AUTO_INCREMENT PRIMARY KEY NOT NULL,
  SPECIES_ID      INTEGER                            NOT NULL,
  LANGUAGE        VARCHAR(8) DEFAULT ''              NOT NULL,
  COUNTRY         VARCHAR(8) DEFAULT ''              NOT NULL,
  VERNACULAR_NAME VARCHAR(256)                       NOT NULL,
  CREATEDATE      TIMESTAMP DEFAULT NOW()
);
CREATE TABLE VISITLOG
(
  USERNAME      VARCHAR(2147483647),
  REQUESTEDURL  VARCHAR(2147483647),
  LOCALES       VARCHAR(2147483647),
  USERAGENT     VARCHAR(2147483647),
  SESSIONID     VARCHAR(2147483647),
  METHOD        VARCHAR(2147483647),
  XFORWARDEDFOR VARCHAR(2147483647),
  CREATEDATE    TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
  REFERER       VARCHAR(2147483647),
  HTTPSTATUS    INTEGER,
  SESSIONAGE    INTEGER,
  HOST          VARCHAR(2147483647),
  QUERYSTRING   VARCHAR(2147483647)
);
ALTER TABLE COMMENTS ADD FOREIGN KEY (SUBMITTER_USER_ID) REFERENCES USERS (ID);
ALTER TABLE COMMENTS ADD FOREIGN KEY (RECEIVER_USER_ID) REFERENCES USERS (ID);
ALTER TABLE MESSAGES ADD FOREIGN KEY (SUBMITTER_USER_ID) REFERENCES USERS (ID);
CREATE INDEX CONSTRAINT_13_INDEX_8 ON MESSAGES (SUBMITTER_USER_ID);
ALTER TABLE PLANTS ADD FOREIGN KEY (SPECIES_ID) REFERENCES SPECIES (ID);
ALTER TABLE PLANTS ADD FOREIGN KEY (USER_ID) REFERENCES USERS (ID);
ALTER TABLE PLANTS ADD FOREIGN KEY (VARIETY_ID) REFERENCES VARIETIES (ID);
CREATE INDEX CONSTRAINT_8CD1_INDEX_2 ON PLANTS (SPECIES_ID);
CREATE INDEX CONSTRAINT_8CD_INDEX_2 ON PLANTS (VARIETY_ID);
CREATE INDEX CONSTRAINT_8C_INDEX_2 ON PLANTS (USER_ID);
ALTER TABLE RULES ADD FOREIGN KEY (HOST) REFERENCES SPECIES (ID);
ALTER TABLE RULES_X_IMPACTS ADD FOREIGN KEY (RULE_ID) REFERENCES RULES (ID);
ALTER TABLE SPECIES ADD FOREIGN KEY (FAMILY_ID) REFERENCES FAMILIES (ID);
ALTER TABLE SPECIES ADD FOREIGN KEY (CREATOR) REFERENCES USERS (ID);
CREATE INDEX CONSTRAINT_B31_INDEX_2 ON SPECIES (CREATOR);
CREATE INDEX CONSTRAINT_B3_INDEX_2 ON SPECIES (FAMILY_ID);
CREATE UNIQUE INDEX UNIQUE_USERS_EMAIL_INDEX_6 ON USERS (EMAIL);
CREATE UNIQUE INDEX UNIQUE_USERS_USERNAME_INDEX_6 ON USERS (USERNAME);
ALTER TABLE VARIETIES ADD FOREIGN KEY (SPECIES_ID) REFERENCES SPECIES (ID);
ALTER TABLE VARIETIES ADD FOREIGN KEY (USER_ID) REFERENCES USERS (ID);
CREATE UNIQUE INDEX UNIQUE_NAME_INDEX_A ON VARIETIES (NAME, SPECIES_ID);
ALTER TABLE VERNACULARS ADD FOREIGN KEY (SPECIES_ID) REFERENCES SPECIES (ID);
CREATE UNIQUE INDEX UNIQUE_VERNACULAR1 ON VERNACULARS (COUNTRY, LANGUAGE, VERNACULAR_NAME);