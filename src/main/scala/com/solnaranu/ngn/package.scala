package com.solnaranu

import scala.util.Random

/**
  * Nigerian utils - NUBAN, account number, bank code, list of banks.
  */
// TODO add testss
// TODO mobile operator codes
package object ngn {

  /**
    * 10 digit NUBAN (Nigerian Universal Bank Account Number) - 9 digit bank account number + a check digit, dependent on the bank code
    */
  type NUBAN = String

  /**
    * 3 digit bank code
    */
  type BankCode = String

  /**
    * 9 digit bank account number (no check digit)
    */
  type AccountNumber = String

  /**
    * NUBAN / Nigerian Universal Account Number utils - generation, validation, check digit...
    *
    * NUBAN is a 10 digit bank account number where digits 1-9 represent the bank account number, while the 10th digit represents the checksum digit.
    *
    * @see https://www.cbn.gov.ng/OUT/2011/CIRCULARS/BSPD/NUBAN%20PROPOSALS%20V%200%204-%2003%2009%202010.PDF
    *
    */
  object NUBAN {
    private[ngn] val RequiredLength = 10

    /**
      * Generates a construction-valid NUBAN. There is a possibility that it's an actively used NUBAN, if an existing bank code is used.
      * The bank code can be provided, otherwise one will be chosen randomly out of the existing, real bank codes (@see [[Bank]])
      * @param bankCode - any 3 digit code will suffice, not necessarily real
      * @return construction-valid NUBAN
      */
    def generate(bankCode: BankCode = BankCode.random()): BankCode = {
      val accountNumber = AccountNumber.generate()
      calculate(accountNumber, bankCode)
    }

    /**
      * Calculates NUBAN from account number and bank code. Does so by calculating the check digit and appending it to the account number.
      *
      * @param accountNumber 9 digits @see [[com.solnaranu.ngn.AccountNumber]]
      * @param bankCode 3 digits @see [[com.solnaranu.ngn.Bank]]
      * @return valid NUBAN (account number + check digit) if input data is valid, throws otherwise
      */
    def calculate(accountNumber: AccountNumber, bankCode: BankCode): NUBAN = {
      AccountNumber.validate(accountNumber)
      BankCode.validate(bankCode)
      accountNumber + calculateCheckDigit(accountNumber, bankCode)
    }

    /**
      * Calculates check digit out of account number and bank code.
      * Algorithm:
      *   1. concatenate bank code and account number
      *   2. multiply each digit from step 1 with digits 3, 7, 3, consecutively and sum up the results
      *   3. Take modulo 10 from step 2 result
      *   4. Substract step 3 result from 10
      *   5. Take modulo 10 of result from step 4
      * @param bankCode 3 digit bank code
      * @param accountNumber 9 digit account number
      * @return check digit (single digit) 0-9
      */
    def calculateCheckDigit(accountNumber: AccountNumber, bankCode: BankCode): Int = {
      val digits: Seq[Int] = (bankCode.toString + accountNumber) map (_.asDigit)
      val multipliers: Seq[Int] = Stream continually Seq(3, 7, 3) flatten
      val productSum = digits zip multipliers map { case (digit, multiplier) => digit * multiplier} sum

      (10 - productSum % 10) % 10
    }

    /**
      * Validates NUBAN for: correct length; all digits; correct check digit (10th digit)
      */
    def validate(nuban: NUBAN, bankCode: BankCode): Unit = {
      BankCode.validate(bankCode)
      require(nuban.length == 10, s"NUBAN must have length $RequiredLength")
      require(nuban.forall(_.isDigit), "NUBAN must be digit only")

      val accountNumber = nuban.take(AccountNumber.RequiredLength)
      val checkDigit = nuban.last.asDigit
      val correctCheckDigit = calculateCheckDigit(accountNumber, bankCode)
      require(checkDigit == correctCheckDigit, s"Check digit $checkDigit is incorrect. Correct: $correctCheckDigit")
    }
  }

  /**
    * 9 digit bank account number, no check digit.
    */
  object AccountNumber {
    private[ngn] val RequiredLength = 9

    /**
      * Generates a random Nigerian account number, without check digit (not NUBAN)
      * @return 9 digit account number
      */
    def generate(): AccountNumber = "%09d".format(Random.nextInt(1000000000))

    /**
      * Validates bank account number for length (@see [[RequiredLength]]) and all digits
      */
    def validate(ban: AccountNumber): Unit = {
      require(ban.length == RequiredLength, s"Bank Account Number must have length $RequiredLength")
      require(ban.forall(_.isDigit), "Bank Account Number must be digit only")
    }
  }

  /**
    * 3 digit bank code
    */
  object BankCode {
    private[ngn] val RequiredLength = 3

    /**
      * Returns a 3 digit bank code from a real, existing, randomly picked Nigerian bank.
      */
    def random(): BankCode = Bank.random().code

    /**
      * Validates bank code for length and all digits.
      */
    def validate(bc: BankCode): Unit = {
      require(bc.length == 3, s"Bank code must have length $RequiredLength")
      require(bc.forall(_.isDigit), "Bank code must have digit only")
    }
  }

  /**
    * A Nigerian bank, represented by it's name and code
    */
  case class Bank(name: String, code: BankCode)

  object Bank {
    val All: IndexedSeq[Bank] = IndexedSeq(
      Bank("ACCESS BANK", "044"),
      Bank("CITIBANK", "023"),
      Bank("DIAMOND BANK", "063"),
      Bank("ECOBANK NIGERIA", "050"),
      Bank("FIDELITY BANK", "070"),
      Bank("FIRST BANK OF NIGERIA", "011"),
      Bank("FIRST CITY MONUMENT BANK", "214"),
      Bank("GUARANTY TRUST BANK", "058"),
      Bank("HERITAGE BANK", "030"),
      Bank("KEYSTONE BANK", "082"),
      Bank("POLARIS BANK", "086"),
      Bank("PROVIDUS BANK", "101"),
      Bank("STANBIC IBTC BANK", "221"),
      Bank("STANDARD CHARTERED BANK", "068"),
      Bank("STERLING BANK", "232"),
      Bank("SUNTRUST", "100"),
      Bank("UNION BANK OF NIGERIA", "032"),
      Bank("UNITED BANK FOR AFRICA", "033"),
      Bank("UNITY BANK", "215"),
      Bank("WEMA BANK", "035"),
      Bank("ZENITH BANK", "057")
    )

    /**
      * Returns a random, real, existing bank, @see [[com.solnaranu.ngn.Bank]]
      */
    def random(): Bank = All(Random.nextInt(All.size))
  }
}
