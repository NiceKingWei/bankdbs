package models

import dao.LoanRow

case class ExLoanRow(customers:Seq[String], loan:LoanRow, states:Option[Int])
