INSERT INTO bankdb.account (account_id, bank_name, money, date) VALUES ('1526973164194', 'bank1', 1000, '2018-05-20');
INSERT INTO bankdb.account (account_id, bank_name, money, date) VALUES ('1526973174923', 'bank1', 1000, '2018-05-20');INSERT INTO bankdb.checking_account (account_id, bank_name, money, date, credit_line) VALUES ('1526973164194', 'bank1', 1000, '2018-05-20', 1000);INSERT INTO bankdb.customer_loan (id_card, loan_number) VALUES ('03', '1526973201470');
INSERT INTO bankdb.customer_loan (id_card, loan_number) VALUES ('04', '1526973201470');INSERT INTO bankdb.customer (id_card, staff_id_card, name, phone, address, contact_name, contact_phone, contact_email, contact_role, role) VALUES ('03', null, 'c1', '55', 'r', 'cc1', '133', 'xx@yy.com', 'f', null);
INSERT INTO bankdb.customer (id_card, staff_id_card, name, phone, address, contact_name, contact_phone, contact_email, contact_role, role) VALUES ('04', '01', 'c2', '55', 'r', 'cc1', '133', 'xx@yy.com', 'f', '贷款负责人');INSERT INTO bankdb.has_account (account_id, id_card, latest_date, is_saving) VALUES ('1526973164194', '03', '2018-05-22', 0);
INSERT INTO bankdb.has_account (account_id, id_card, latest_date, is_saving) VALUES ('1526973174923', '03', '2018-05-22', 1);INSERT INTO bankdb.loan_pay (pay_id, loan_number, date, money) VALUES ('1526973213057', '1526973201470', '2018-05-21', 150);
INSERT INTO bankdb.loan_pay (pay_id, loan_number, date, money) VALUES ('1526973218708', '1526973201470', '2018-05-22', 150);INSERT INTO bankdb.loan (loan_number, bank_name, amount) VALUES ('1526973201470', 'bank2', 300);INSERT INTO bankdb.saving_account (account_id, bank_name, money, date, rate, currency) VALUES ('1526973174923', 'bank1', 1000, '2018-05-20', 0.01, 'RMB');INSERT INTO bankdb.staff (staff_id_card, bank_name, name, phone, address, start_date) VALUES ('01', 'bank1', 'n1', '133555', 'x', '2018-01-01');
INSERT INTO bankdb.staff (staff_id_card, bank_name, name, phone, address, start_date) VALUES ('02', 'bank1', 'n2', '155', 'x', '2018-01-01');INSERT INTO bankdb.subbranch (bank_name, city, money) VALUES ('bank1', 'c1', 30000);
INSERT INTO bankdb.subbranch (bank_name, city, money) VALUES ('bank2', 'c2', 20000);