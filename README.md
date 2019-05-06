# A little work in progress lib for NUBAN (**N**igerian **U**niversal **A**ccount **N**umber)

## Contains
* Validator
* Generator / Calculator
* Check digit calculator

# Usage examples

```
import com.solnaranu.ngn._

...
  NUBAN.generate()
  NUBAN.generate(bankCode = "044")
  NUBAN.generate(bankCode = Bank.random().code)
  NUBAN.calculate(accountNumber = "172318511", bankCode = "033")
  NUBAN.calculateCheckDigit(accountNumber = "172318511", bankCode = "033")
  NUBAN.validate(nuban = "1723185117", bankCode = "033")

  AccountNumber.generate()
  AccountNumber.validate("987654321")

  BankCode.random()
  BankCode.validate("123")

  val imaginaryBank = Bank("Imaginary Bank", "999")
  val banks = Bank.All
...
```
