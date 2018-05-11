-- create databse bankdb;

drop table if exists account;

drop table if exists checking_account;

drop table if exists customer;

drop table if exists customer_loan;

drop table if exists department;

drop table if exists has_account;

drop table if exists loan;

drop table if exists loan_pay;

drop table if exists saving_account;

drop table if exists staff;

drop table if exists subbank;

/*==============================================================*/
/* Table: account                                               */
/*==============================================================*/
create table account
(
   account_id           char(255) not null,
   bank_name            char(64) not null,
   money                double not null,
   date                 date not null,
   primary key (account_id)
);

/*==============================================================*/
/* Table: checking_account                                      */
/*==============================================================*/
create table checking_account
(
   account_id           char(255) not null,
   bank_name            char(64),
   money                double not null,
   date                 date not null,
   credit_line          double not null,
   primary key (account_id)
);

/*==============================================================*/
/* Table: customer                                              */
/*==============================================================*/
create table customer
(
   id_card              char(64) not null,
   staff_id_card        char(255),
   name                 char(64) not null,
   phone                char(16) not null,
   address              text not null,
   contact_name         char(64) not null,
   contact_phone        char(16) not null,
   contact_email        char(255) not null,
   contact_role         char(64) not null,
   role                 char(16),
   primary key (id_card)
);

/*==============================================================*/
/* Table: customer_loan                                         */
/*==============================================================*/
create table customer_loan
(
   id_card              char(64) not null,
   loan_number          char(255) not null,
   primary key (id_card, loan_number)
);

/*==============================================================*/
/* Table: department                                            */
/*==============================================================*/
create table department
(
   department_num       numeric(8,0) not null,
   bank_name            char(64) not null,
   staff_id_card        char(255) not null,
   name                 char(64) not null,
   primary key (department_num)
);

/*==============================================================*/
/* Table: has_account                                           */
/*==============================================================*/
create table has_account
(
   account_id           char(255) not null,
   id_card              char(64) not null,
   latest_date          date,
   is_saving            bool,
   primary key (account_id, id_card)
);

/*==============================================================*/
/* Table: loan                                                  */
/*==============================================================*/
create table loan
(
   loan_number          char(255) not null,
   bank_name            char(64) not null,
   amount               double not null,
   primary key (loan_number)
);

/*==============================================================*/
/* Table: loan_pay                                              */
/*==============================================================*/
create table loan_pay
(
   pay_id               char(255) not null,
   loan_number          char(255) not null,
   date                 date not null,
   money                double not null,
   primary key (pay_id)
);

/*==============================================================*/
/* Table: saving_account                                        */
/*==============================================================*/
create table saving_account
(
   account_id           char(255) not null,
   bank_name            char(64),
   money                double not null,
   date                 date not null,
   rate                 double not null,
   currency             char(255) not null,
   primary key (account_id)
);

/*==============================================================*/
/* Table: staff                                                 */
/*==============================================================*/
create table staff
(
   staff_id_card        char(255) not null,
   department_num       numeric(8,0) not null,
   name                 char(64) not null,
   phone                char(16) not null,
   address              text not null,
   start_date           date not null,
   primary key (staff_id_card)
);

/*==============================================================*/
/* Table: subbank                                               */
/*==============================================================*/
create table subbank
(
   bank_name            char(64) not null,
   city                 char(255) not null,
   money                double not null,
   primary key (bank_name)
);

alter table account add constraint FK_subbank_account_relationship foreign key (bank_name)
      references subbank (bank_name) on delete restrict on update restrict;

alter table checking_account add constraint FK_Inheritance_3 foreign key (account_id)
      references account (account_id) on delete restrict on update restrict;

alter table customer add constraint FK_staff_customer_association foreign key (staff_id_card)
      references staff (staff_id_card) on delete restrict on update restrict;

alter table customer_loan add constraint FK_customer_loan foreign key (id_card)
      references customer (id_card) on delete restrict on update restrict;

alter table customer_loan add constraint FK_customer_loan2 foreign key (loan_number)
      references loan (loan_number) on delete restrict on update restrict;

alter table department add constraint FK_bank_dept foreign key (bank_name)
      references subbank (bank_name) on delete restrict on update restrict;

alter table department add constraint FK_manager_relationship foreign key (staff_id_card)
      references staff (staff_id_card) on delete restrict on update restrict;

alter table has_account add constraint FK_has_account foreign key (account_id)
      references account (account_id) on delete restrict on update restrict;

alter table has_account add constraint FK_has_account2 foreign key (id_card)
      references customer (id_card) on delete restrict on update restrict;

alter table loan add constraint FK_bank_loan_relationship foreign key (bank_name)
      references subbank (bank_name) on delete restrict on update restrict;

alter table loan_pay add constraint FK_pay foreign key (loan_number)
      references loan (loan_number) on delete restrict on update restrict;

alter table saving_account add constraint FK_Inheritance_2 foreign key (account_id)
      references account (account_id) on delete restrict on update restrict;

alter table staff add constraint FK_staff_department_relationship foreign key (department_num)
      references department (department_num) on delete restrict on update restrict;