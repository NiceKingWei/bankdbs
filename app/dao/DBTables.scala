package dao
// AUTO-GENERATED Slick data model

/** Entity class storing rows of table Subbranch
  *  @param bankName Database column bank_name SqlType(CHAR), PrimaryKey, Length(64,false)
  *  @param city Database column city SqlType(CHAR), Length(255,false)
  *  @param money Database column money SqlType(DOUBLE) */
case class SubbranchRow(bankName: String, city: String, money: Double)

/** Entity class storing rows of table Account
  *  @param accountId Database column account_id SqlType(CHAR), PrimaryKey, Length(255,false)
  *  @param bankName Da tabase column bank_name SqlType(CHAR), Length(64,false)
  *  @param money Database column money SqlType(DOUBLE)
  *  @param date Database column date SqlType(DATE) */
case class AccountRow(accountId: String, bankName: String, money: Double, date: java.sql.Date)

/** Entity class storing rows of table Staff
  *  @param staffIdCard Database column staff_id_card SqlType(CHAR), PrimaryKey, Length(255,false)
  *  @param bankName Database column bank_name SqlType(CHAR), Length(64,false)
  *  @param name Database column name SqlType(CHAR), Length(64,false)
  *  @param phone Database column phone SqlType(CHAR), Length(16,false)
  *  @param address Database column address SqlType(TEXT)
  *  @param startDate Database column start_date SqlType(DATE) */
case class StaffRow(staffIdCard: String, bankName: String, name: String, phone: String, address: String, startDate: java.sql.Date)

/** Entity class storing rows of table Customer
  *  @param idCard Database column id_card SqlType(CHAR), PrimaryKey, Length(64,false)
  *  @param staffIdCard Database column staff_id_card SqlType(CHAR), Length(255,false), Default(None)
  *  @param name Database column name SqlType(CHAR), Length(64,false)
  *  @param phone Database column phone SqlType(CHAR), Length(16,false)
  *  @param address Database column address SqlType(TEXT)
  *  @param contactName Database column contact_name SqlType(CHAR), Length(64,false)
  *  @param contactPhone Database column contact_phone SqlType(CHAR), Length(16,false)
  *  @param contactEmail Database column contact_email SqlType(CHAR), Length(255,false)
  *  @param contactRole Database column contact_role SqlType(CHAR), Length(64,false)
  *  @param role Database column role SqlType(CHAR), Length(16,false), Default(None) */
case class CustomerRow(idCard: String, staffIdCard: Option[String] = None, name: String, phone: String, address: String, contactName: String, contactPhone: String, contactEmail: String, contactRole: String, role: Option[String] = None)

/** Entity class storing rows of table CheckingAccount
  *  @param accountId Database column account_id SqlType(CHAR), PrimaryKey, Length(255,false)
  *  @param bankName Database column bank_name SqlType(CHAR), Length(64,false), Default(None)
  *  @param money Database column money SqlType(DOUBLE)
  *  @param date Database column date SqlType(DATE)
  *  @param creditLine Database column credit_line SqlType(DOUBLE) */
case class CheckingAccountRow(accountId: String, bankName: Option[String] = None, money: Double, date: java.sql.Date, creditLine: Double)

/** Entity class storing rows of table SavingAccount
  *  @param accountId Database column account_id SqlType(CHAR), PrimaryKey, Length(255,false)
  *  @param bankName Database column bank_name SqlType(CHAR), Length(64,false), Default(None)
  *  @param money Database column money SqlType(DOUBLE)
  *  @param date Database column date SqlType(DATE)
  *  @param rate Database column rate SqlType(DOUBLE)
  *  @param currency Database column currency SqlType(CHAR), Length(255,false) */
case class SavingAccountRow(accountId: String, bankName: Option[String] = None, money: Double, date: java.sql.Date, rate: Double, currency: String)


/** Entity class storing rows of table HasAccount
  *  @param accountId Database column account_id SqlType(CHAR), Length(255,false)
  *  @param idCard Database column id_card SqlType(CHAR), Length(64,false)
  *  @param latestDate Database column latest_date SqlType(DATE), Default(None)
  *  @param isSaving Database column is_saving SqlType(BIT), Default(None) */
case class HasAccountRow(accountId: String, idCard: String, latestDate: Option[java.sql.Date] = None, isSaving: Option[Boolean] = None)


/** Entity class storing rows of table Loan
  *  @param loanNumber Database column loan_number SqlType(CHAR), PrimaryKey, Length(255,false)
  *  @param bankName Database column bank_name SqlType(CHAR), Length(64,false)
  *  @param amount Database column amount SqlType(DOUBLE) */
case class LoanRow(loanNumber: String, bankName: String, amount: Double)


/** Entity class storing rows of table CustomerLoan
  *  @param idCard Database column id_card SqlType(CHAR), Length(64,false)
  *  @param loanNumber Database column loan_number SqlType(CHAR), Length(255,false) */
case class CustomerLoanRow(idCard: String, loanNumber: String)

/** Entity class storing rows of table LoanPay
  *  @param payId Database column pay_id SqlType(CHAR), PrimaryKey, Length(255,false)
  *  @param loanNumber Database column loan_number SqlType(CHAR), Length(255,false)
  *  @param date Database column date SqlType(DATE)
  *  @param money Database column money SqlType(DOUBLE) */
  case class LoanPayRow(payId: String, loanNumber: String, date: java.sql.Date, money: Double)

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait DBTables {
  val profile = slick.jdbc.MySQLProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Account.schema, CheckingAccount.schema, Customer.schema, CustomerLoan.schema, HasAccount.schema, Loan.schema, LoanPay.schema, SavingAccount.schema, Staff.schema, Subbranch.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** GetResult implicit for fetching AccountRow objects using plain SQL queries */
  implicit def GetResultAccountRow(implicit e0: GR[String], e1: GR[Double], e2: GR[java.sql.Date]): GR[AccountRow] = GR{
    prs => import prs._
      AccountRow.tupled((<<[String], <<[String], <<[Double], <<[java.sql.Date]))
  }
  /** Table description of table account. Objects of this class serve as prototypes for rows in queries. */
  class Account(_tableTag: Tag) extends profile.api.Table[AccountRow](_tableTag, Some("bankdb"), "account") {
    def * = (accountId, bankName, money, date) <> (AccountRow.tupled, AccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), Rep.Some(bankName), Rep.Some(money), Rep.Some(date)).shaped.<>({r=>import r._; _1.map(_=> AccountRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(CHAR), PrimaryKey, Length(255,false) */
    val accountId: Rep[String] = column[String]("account_id", O.PrimaryKey, O.Length(255,varying=false))
    /** Database column bank_name SqlType(CHAR), Length(64,false) */
    val bankName: Rep[String] = column[String]("bank_name", O.Length(64,varying=false))
    /** Database column money SqlType(DOUBLE) */
    val money: Rep[Double] = column[Double]("money")
    /** Database column date SqlType(DATE) */
    val date: Rep[java.sql.Date] = column[java.sql.Date]("date")

    /** Foreign key referencing Subbranch (database name FK_subbank_account_relationship) */
    lazy val subbranchFk = foreignKey("FK_subbank_account_relationship", bankName, Subbranch)(r => r.bankName, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Account */
  lazy val Account = new TableQuery(tag => new Account(tag))


  /** GetResult implicit for fetching CheckingAccountRow objects using plain SQL queries */
  implicit def GetResultCheckingAccountRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Double], e3: GR[java.sql.Date]): GR[CheckingAccountRow] = GR{
    prs => import prs._
      CheckingAccountRow.tupled((<<[String], <<?[String], <<[Double], <<[java.sql.Date], <<[Double]))
  }
  /** Table description of table checking_account. Objects of this class serve as prototypes for rows in queries. */
  class CheckingAccount(_tableTag: Tag) extends profile.api.Table[CheckingAccountRow](_tableTag, Some("bankdb"), "checking_account") {
    def * = (accountId, bankName, money, date, creditLine) <> (CheckingAccountRow.tupled, CheckingAccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), bankName, Rep.Some(money), Rep.Some(date), Rep.Some(creditLine)).shaped.<>({r=>import r._; _1.map(_=> CheckingAccountRow.tupled((_1.get, _2, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(CHAR), PrimaryKey, Length(255,false) */
    val accountId: Rep[String] = column[String]("account_id", O.PrimaryKey, O.Length(255,varying=false))
    /** Database column bank_name SqlType(CHAR), Length(64,false), Default(None) */
    val bankName: Rep[Option[String]] = column[Option[String]]("bank_name", O.Length(64,varying=false), O.Default(None))
    /** Database column money SqlType(DOUBLE) */
    val money: Rep[Double] = column[Double]("money")
    /** Database column date SqlType(DATE) */
    val date: Rep[java.sql.Date] = column[java.sql.Date]("date")
    /** Database column credit_line SqlType(DOUBLE) */
    val creditLine: Rep[Double] = column[Double]("credit_line")

    /** Foreign key referencing Account (database name FK_Inheritance_3) */
    lazy val accountFk = foreignKey("FK_Inheritance_3", accountId, Account)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table CheckingAccount */
  lazy val CheckingAccount = new TableQuery(tag => new CheckingAccount(tag))


  /** GetResult implicit for fetching CustomerRow objects using plain SQL queries */
  implicit def GetResultCustomerRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[CustomerRow] = GR{
    prs => import prs._
      CustomerRow.tupled((<<[String], <<?[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<?[String]))
  }
  /** Table description of table customer. Objects of this class serve as prototypes for rows in queries. */
  class Customer(_tableTag: Tag) extends profile.api.Table[CustomerRow](_tableTag, Some("bankdb"), "customer") {
    def * = (idCard, staffIdCard, name, phone, address, contactName, contactPhone, contactEmail, contactRole, role) <> (CustomerRow.tupled, CustomerRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(idCard), staffIdCard, Rep.Some(name), Rep.Some(phone), Rep.Some(address), Rep.Some(contactName), Rep.Some(contactPhone), Rep.Some(contactEmail), Rep.Some(contactRole), role).shaped.<>({r=>import r._; _1.map(_=> CustomerRow.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id_card SqlType(CHAR), PrimaryKey, Length(64,false) */
    val idCard: Rep[String] = column[String]("id_card", O.PrimaryKey, O.Length(64,varying=false))
    /** Database column staff_id_card SqlType(CHAR), Length(255,false), Default(None) */
    val staffIdCard: Rep[Option[String]] = column[Option[String]]("staff_id_card", O.Length(255,varying=false), O.Default(None))
    /** Database column name SqlType(CHAR), Length(64,false) */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=false))
    /** Database column phone SqlType(CHAR), Length(16,false) */
    val phone: Rep[String] = column[String]("phone", O.Length(16,varying=false))
    /** Database column address SqlType(TEXT) */
    val address: Rep[String] = column[String]("address")
    /** Database column contact_name SqlType(CHAR), Length(64,false) */
    val contactName: Rep[String] = column[String]("contact_name", O.Length(64,varying=false))
    /** Database column contact_phone SqlType(CHAR), Length(16,false) */
    val contactPhone: Rep[String] = column[String]("contact_phone", O.Length(16,varying=false))
    /** Database column contact_email SqlType(CHAR), Length(255,false) */
    val contactEmail: Rep[String] = column[String]("contact_email", O.Length(255,varying=false))
    /** Database column contact_role SqlType(CHAR), Length(64,false) */
    val contactRole: Rep[String] = column[String]("contact_role", O.Length(64,varying=false))
    /** Database column role SqlType(CHAR), Length(16,false), Default(None) */
    val role: Rep[Option[String]] = column[Option[String]]("role", O.Length(16,varying=false), O.Default(None))

    /** Foreign key referencing Staff (database name FK_staff_customer_association) */
    lazy val staffFk = foreignKey("FK_staff_customer_association", staffIdCard, Staff)(r => Rep.Some(r.staffIdCard), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Customer */
  lazy val Customer = new TableQuery(tag => new Customer(tag))


  /** GetResult implicit for fetching CustomerLoanRow objects using plain SQL queries */
  implicit def GetResultCustomerLoanRow(implicit e0: GR[String]): GR[CustomerLoanRow] = GR{
    prs => import prs._
      CustomerLoanRow.tupled((<<[String], <<[String]))
  }
  /** Table description of table customer_loan. Objects of this class serve as prototypes for rows in queries. */
  class CustomerLoan(_tableTag: Tag) extends profile.api.Table[CustomerLoanRow](_tableTag, Some("bankdb"), "customer_loan") {
    def * = (idCard, loanNumber) <> (CustomerLoanRow.tupled, CustomerLoanRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(idCard), Rep.Some(loanNumber)).shaped.<>({r=>import r._; _1.map(_=> CustomerLoanRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id_card SqlType(CHAR), Length(64,false) */
    val idCard: Rep[String] = column[String]("id_card", O.Length(64,varying=false))
    /** Database column loan_number SqlType(CHAR), Length(255,false) */
    val loanNumber: Rep[String] = column[String]("loan_number", O.Length(255,varying=false))

    /** Primary key of CustomerLoan (database name customer_loan_PK) */
    val pk = primaryKey("customer_loan_PK", (idCard, loanNumber))

    /** Foreign key referencing Customer (database name FK_customer_loan) */
    lazy val customerFk = foreignKey("FK_customer_loan", idCard, Customer)(r => r.idCard, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Loan (database name FK_customer_loan2) */
    lazy val loanFk = foreignKey("FK_customer_loan2", loanNumber, Loan)(r => r.loanNumber, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table CustomerLoan */
  lazy val CustomerLoan = new TableQuery(tag => new CustomerLoan(tag))


  /** GetResult implicit for fetching HasAccountRow objects using plain SQL queries */
  implicit def GetResultHasAccountRow(implicit e0: GR[String], e1: GR[Option[java.sql.Date]], e2: GR[Option[Boolean]]): GR[HasAccountRow] = GR{
    prs => import prs._
      HasAccountRow.tupled((<<[String], <<[String], <<?[java.sql.Date], <<?[Boolean]))
  }
  /** Table description of table has_account. Objects of this class serve as prototypes for rows in queries. */
  class HasAccount(_tableTag: Tag) extends profile.api.Table[HasAccountRow](_tableTag, Some("bankdb"), "has_account") {
    def * = (accountId, idCard, latestDate, isSaving) <> (HasAccountRow.tupled, HasAccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), Rep.Some(idCard), latestDate, isSaving).shaped.<>({r=>import r._; _1.map(_=> HasAccountRow.tupled((_1.get, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(CHAR), Length(255,false) */
    val accountId: Rep[String] = column[String]("account_id", O.Length(255,varying=false))
    /** Database column id_card SqlType(CHAR), Length(64,false) */
    val idCard: Rep[String] = column[String]("id_card", O.Length(64,varying=false))
    /** Database column latest_date SqlType(DATE), Default(None) */
    val latestDate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("latest_date", O.Default(None))
    /** Database column is_saving SqlType(BIT), Default(None) */
    val isSaving: Rep[Option[Boolean]] = column[Option[Boolean]]("is_saving", O.Default(None))

    /** Primary key of HasAccount (database name has_account_PK) */
    val pk = primaryKey("has_account_PK", (accountId, idCard))

    /** Foreign key referencing Account (database name FK_has_account) */
    lazy val accountFk = foreignKey("FK_has_account", accountId, Account)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Customer (database name FK_has_account2) */
    lazy val customerFk = foreignKey("FK_has_account2", idCard, Customer)(r => r.idCard, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table HasAccount */
  lazy val HasAccount = new TableQuery(tag => new HasAccount(tag))


  /** GetResult implicit for fetching LoanRow objects using plain SQL queries */
  implicit def GetResultLoanRow(implicit e0: GR[String], e1: GR[Double]): GR[LoanRow] = GR{
    prs => import prs._
      LoanRow.tupled((<<[String], <<[String], <<[Double]))
  }
  /** Table description of table loan. Objects of this class serve as prototypes for rows in queries. */
  class Loan(_tableTag: Tag) extends profile.api.Table[LoanRow](_tableTag, Some("bankdb"), "loan") {
    def * = (loanNumber, bankName, amount) <> (LoanRow.tupled, LoanRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(loanNumber), Rep.Some(bankName), Rep.Some(amount)).shaped.<>({r=>import r._; _1.map(_=> LoanRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column loan_number SqlType(CHAR), PrimaryKey, Length(255,false) */
    val loanNumber: Rep[String] = column[String]("loan_number", O.PrimaryKey, O.Length(255,varying=false))
    /** Database column bank_name SqlType(CHAR), Length(64,false) */
    val bankName: Rep[String] = column[String]("bank_name", O.Length(64,varying=false))
    /** Database column amount SqlType(DOUBLE) */
    val amount: Rep[Double] = column[Double]("amount")

    /** Foreign key referencing Subbranch (database name FK_bank_loan_relationship) */
    lazy val subbranchFk = foreignKey("FK_bank_loan_relationship", bankName, Subbranch)(r => r.bankName, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Loan */
  lazy val Loan = new TableQuery(tag => new Loan(tag))


  /** GetResult implicit for fetching LoanPayRow objects using plain SQL queries */
  implicit def GetResultLoanPayRow(implicit e0: GR[String], e1: GR[java.sql.Date], e2: GR[Double]): GR[LoanPayRow] = GR{
    prs => import prs._
      LoanPayRow.tupled((<<[String], <<[String], <<[java.sql.Date], <<[Double]))
  }
  /** Table description of table loan_pay. Objects of this class serve as prototypes for rows in queries. */
  class LoanPay(_tableTag: Tag) extends profile.api.Table[LoanPayRow](_tableTag, Some("bankdb"), "loan_pay") {
    def * = (payId, loanNumber, date, money) <> (LoanPayRow.tupled, LoanPayRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(payId), Rep.Some(loanNumber), Rep.Some(date), Rep.Some(money)).shaped.<>({r=>import r._; _1.map(_=> LoanPayRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column pay_id SqlType(CHAR), PrimaryKey, Length(255,false) */
    val payId: Rep[String] = column[String]("pay_id", O.PrimaryKey, O.Length(255,varying=false))
    /** Database column loan_number SqlType(CHAR), Length(255,false) */
    val loanNumber: Rep[String] = column[String]("loan_number", O.Length(255,varying=false))
    /** Database column date SqlType(DATE) */
    val date: Rep[java.sql.Date] = column[java.sql.Date]("date")
    /** Database column money SqlType(DOUBLE) */
    val money: Rep[Double] = column[Double]("money")

    /** Foreign key referencing Loan (database name FK_pay) */
    lazy val loanFk = foreignKey("FK_pay", loanNumber, Loan)(r => r.loanNumber, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table LoanPay */
  lazy val LoanPay = new TableQuery(tag => new LoanPay(tag))


  /** GetResult implicit for fetching SavingAccountRow objects using plain SQL queries */
  implicit def GetResultSavingAccountRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Double], e3: GR[java.sql.Date]): GR[SavingAccountRow] = GR{
    prs => import prs._
      SavingAccountRow.tupled((<<[String], <<?[String], <<[Double], <<[java.sql.Date], <<[Double], <<[String]))
  }
  /** Table description of table saving_account. Objects of this class serve as prototypes for rows in queries. */
  class SavingAccount(_tableTag: Tag) extends profile.api.Table[SavingAccountRow](_tableTag, Some("bankdb"), "saving_account") {
    def * = (accountId, bankName, money, date, rate, currency) <> (SavingAccountRow.tupled, SavingAccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), bankName, Rep.Some(money), Rep.Some(date), Rep.Some(rate), Rep.Some(currency)).shaped.<>({r=>import r._; _1.map(_=> SavingAccountRow.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(CHAR), PrimaryKey, Length(255,false) */
    val accountId: Rep[String] = column[String]("account_id", O.PrimaryKey, O.Length(255,varying=false))
    /** Database column bank_name SqlType(CHAR), Length(64,false), Default(None) */
    val bankName: Rep[Option[String]] = column[Option[String]]("bank_name", O.Length(64,varying=false), O.Default(None))
    /** Database column money SqlType(DOUBLE) */
    val money: Rep[Double] = column[Double]("money")
    /** Database column date SqlType(DATE) */
    val date: Rep[java.sql.Date] = column[java.sql.Date]("date")
    /** Database column rate SqlType(DOUBLE) */
    val rate: Rep[Double] = column[Double]("rate")
    /** Database column currency SqlType(CHAR), Length(255,false) */
    val currency: Rep[String] = column[String]("currency", O.Length(255,varying=false))

    /** Foreign key referencing Account (database name FK_Inheritance_2) */
    lazy val accountFk = foreignKey("FK_Inheritance_2", accountId, Account)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table SavingAccount */
  lazy val SavingAccount = new TableQuery(tag => new SavingAccount(tag))

  /** GetResult implicit for fetching StaffRow objects using plain SQL queries */
  implicit def GetResultStaffRow(implicit e0: GR[String], e1: GR[java.sql.Date]): GR[StaffRow] = GR{
    prs => import prs._
      StaffRow.tupled((<<[String], <<[String], <<[String], <<[String], <<[String], <<[java.sql.Date]))
  }
  /** Table description of table staff. Objects of this class serve as prototypes for rows in queries. */
  class Staff(_tableTag: Tag) extends profile.api.Table[StaffRow](_tableTag, Some("bankdb"), "staff") {
    def * = (staffIdCard, bankName, name, phone, address, startDate) <> (StaffRow.tupled, StaffRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(staffIdCard), Rep.Some(bankName), Rep.Some(name), Rep.Some(phone), Rep.Some(address), Rep.Some(startDate)).shaped.<>({r=>import r._; _1.map(_=> StaffRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column staff_id_card SqlType(CHAR), PrimaryKey, Length(255,false) */
    val staffIdCard: Rep[String] = column[String]("staff_id_card", O.PrimaryKey, O.Length(255,varying=false))
    /** Database column bank_name SqlType(CHAR), Length(64,false) */
    val bankName: Rep[String] = column[String]("bank_name", O.Length(64,varying=false))
    /** Database column name SqlType(CHAR), Length(64,false) */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=false))
    /** Database column phone SqlType(CHAR), Length(16,false) */
    val phone: Rep[String] = column[String]("phone", O.Length(16,varying=false))
    /** Database column address SqlType(TEXT) */
    val address: Rep[String] = column[String]("address")
    /** Database column start_date SqlType(DATE) */
    val startDate: Rep[java.sql.Date] = column[java.sql.Date]("start_date")

    /** Foreign key referencing Subbranch (database name FK_Relationship_5) */
    lazy val subbranchFk = foreignKey("FK_Relationship_5", bankName, Subbranch)(r => r.bankName, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Staff */
  lazy val Staff = new TableQuery(tag => new Staff(tag))


  /** GetResult implicit for fetching SubbranchRow objects using plain SQL queries */
  implicit def GetResultSubbranchRow(implicit e0: GR[String], e1: GR[Double]): GR[SubbranchRow] = GR{
    prs => import prs._
      SubbranchRow.tupled((<<[String], <<[String], <<[Double]))
  }
  /** Table description of table subbranch. Objects of this class serve as prototypes for rows in queries. */
  class Subbranch(_tableTag: Tag) extends profile.api.Table[SubbranchRow](_tableTag, Some("bankdb"), "subbranch") {
    def * = (bankName, city, money) <> (SubbranchRow.tupled, SubbranchRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(bankName), Rep.Some(city), Rep.Some(money)).shaped.<>({r=>import r._; _1.map(_=> SubbranchRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column bank_name SqlType(CHAR), PrimaryKey, Length(64,false) */
    val bankName: Rep[String] = column[String]("bank_name", O.PrimaryKey, O.Length(64,varying=false))
    /** Database column city SqlType(CHAR), Length(255,false) */
    val city: Rep[String] = column[String]("city", O.Length(255,varying=false))
    /** Database column money SqlType(DOUBLE) */
    val money: Rep[Double] = column[Double]("money")
  }
  /** Collection-like TableQuery object for table Subbranch */
  lazy val Subbranch = new TableQuery(tag => new Subbranch(tag))
}
