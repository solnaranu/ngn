# A little work in progress lib for NUBAN (**N**igerian **U**niversal **B** **A**ccount **N**umber)

## Contains
* Validator
* Generator / Calculator
* Check digit calculator

# Usage examples

```
import com.solnaranu.ngn._

...

NUBAN.generate() // generated construction-valid NUBAN
NUBAN.generate(bankCode = "044") // generate construction-valid NUBAN for bank code 044
NUBAN.generate(bankCode = Bank.random().code) // generate construction-valid NUBAN for a random (but real) bank code
NUBAN.calculate(accountNumber = "172318511", bankCode = "033") // calculate NUBAN for account number and bank coude
NUBAN.calculateCheckDigit(accountNumber = "172318511", bankCode = "033") // calculate NUBAN check digit for account number and bank code
NUBAN.validate(nuban = "1723185117", bankCode = "033") // validate NUBAN for bank code 033

AccountNumber.generate() // generate random account number (just random 9 digits)
AccountNumber.validate("987654321") // validate account number

BankCode.random() // random, but real, bank code
BankCode.validate("123") // validate bank code

val imaginaryBank = Bank("Imaginary Bank", "999") // construct bank instance
val realBank = Bank.random() // get a random, but real, bank
val banks = Bank.All // get a list of all banks

...
```
