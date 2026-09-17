ACME Bank CLI System

ACME Bank is a Java application that is developed using the command line. It includes two user roles, Banker and Customer, and provides examples of object-oriented programming, authentication, file persistence, banking operations, transaction and debit card limits, and exception handling.

GitHub Repository

ACME Bank CLI System

Technologies Used

Java 17

IntelliJ IDEA

Object-Oriented Programming

Java Collections and Streams

Lambda expressions and Optional

RandomAccessFile

SHA-256 password hashing

Custom Exceptions

JUnit 5

Git, GitHub, and Trello

Main Features

Authentication and Security

Login as Customer or Banker.

Passwords are not stored in clear text, but in SHA-256 hash form.

A temporary password should be changed on the first login by a new user.

A user is locked for one minute after three incorrect login attempts.

Banker Features

Add a Customer.

Open a Checking or Savings account, or both.

Avoid duplicate Customer IDs, User IDs, and Account IDs.

Provide an automatic Mastercard for newly opened accounts.

View customers and their transaction records.

Review, approve, or reject debit card upgrade requests.

Customer Features

View personal accounts and balances.

Deposit and withdraw funds.

Transfer between personal accounts or to another customer.

Check transaction details and the account statement.

Apply the filters Today, Yesterday, Last Week, Last 7 Days, Last Month, or Last 30 Days.

Ask to have a debit card upgrade.

Debit Cards and Daily Limits

All new accounts will be issued the standard Mastercard. The type of card is subject to change only after the Customer's request and the Banker's acceptance.

Operation

Mastercard

Titanium

Platinum

Daily Withdrawal

$5,000

$10,000

$20,000

Daily Transfer

$10,000

$20,000

$40,000

Daily Own-Account Transfer

$20,000

$40,000

$80,000

Daily Deposit

$100,000

$100,000

$100,000

Daily Own-Account Deposit

$200,000

$200,000

$200,000

Daily usage is stored with the date it was used. At the start of a new day, the values for withdrawal, transfer, own-transfer, and deposit are cleared.

Overdraft Protection

An overdraft charge of $35 will be incurred if a withdrawal or transfer results in an overdraft.

An operation can use no more than $100 beyond the available balance.

If the account has a negative balance, any withdrawal or transfer of more than $100 is declined.

An account becomes inactive after two incidences of overdraft.

If enough money is deposited to make the balance equal to or greater than $0, the account is reactivated and the overdraft count is reset.

Transaction Tracking

A deposit will be entered as DEPOSIT.

A withdrawal will be entered as WITHDRAW.

When creating a transfer, two records are created: TRANSFER_OUT for the source account and TRANSFER_IN for the destination account.

To make the transfer records easier for users to understand, they are presented as SENT or RECEIVED.

Each transaction stores its ID, Account ID, transaction type, amount, balance after the transaction, related account if applicable, and date/time.

Project Structure

src/
|-- BankApplication/
|   |-- Main.java
|   |-- BankerMenu.java
|   `-- CustomerMenu.java
|-- BankingSystem/
|   `-- BankSystem.java
|-- BankModels/
|   |-- User.java
|   |-- Banker.java
|   |-- Customer.java
|   |-- Account.java
|   |-- DebitCard.java
|   `-- Transaction.java
|-- BankServices/
|   |-- BankingOperations.java
|   |-- LoginService.java
|   |-- CustomerService.java
|   |-- AccountService.java
|   |-- DebitCardService.java
|   `-- TransactionService.java
|-- BankUtilities/
|   |-- FileHandlingUtility.java
|   `-- PasswordHashingUtility.java
`-- BankExceptions/
|-- AccountLockedException.java
|-- DailyLimitExceededException.java
`-- InactiveAccountException.java

Exception Handling

The project uses custom exceptions to make errors clear and to prevent invalid operations:

AccountLockedException is used when a user is still locked or reaches three failed login attempts.

DailyLimitExceededException is used when a withdrawal would exceed the daily card limit.

InactiveAccountException is used when a withdrawal is attempted from an inactive account. Transfers from inactive accounts are rejected by the transfer validation.

These exceptions extend RuntimeException. They are thrown by the service classes and handled by the application menus so the user receives a clear message instead of the banking operation continuing.

Unit Testing

JUnit 5 was used to test individual parts of the application:

AccountTest checks that a new account stores the correct Account ID, Customer ID, account type, and balance. It also checks that a new account is active and starts with an overdraft count of zero.

DebitCardServiceTest checks the withdrawal limits for Mastercard, Titanium, and Platinum. It also checks the Mastercard transfer, own-account transfer, and deposit limits.

The tests use assertEquals() to compare expected and actual values and assertTrue() to confirm that a new account is active.

Planning and Development Process

Firstly, I updated the requirements and turned them into user stories for the Banker and Customer roles. I designed the entities and the relationships between these entities, and expanded the project in increments:

Identified the model types for the users, accounts, cards, and transactions.

Implemented user login, accounts, and services for customers, cards, and transactions.

Added role-based command-line menus.

Implemented text-file persistence so data persists when the application restarts.

Implemented password hashing, failed login tracking, account locking, and custom exceptions.

Added banking operations, overdraft protection, daily limits, statements, transaction filters, and card upgrades.

Evaluated each feature with valid, invalid, boundary, and persistence tests.

Problem-Solving Strategy

I tested a single feature at a time. Whenever a problem was identified through a test, I traced the data through the menu, service, model, and file-handling layers. I corrected the related validation and tested it again to see if the balances, transaction records, account status, and saved files were consistent.

Examples include avoiding duplicate IDs, disallowing invalid amounts, customer-to-customer source account validation, daily limits, and ensuring failed operations do not change balances or transactions.

Favorite Functions

Secure Login

The entered password is hashed by LoginService.login(), and the saved password hash is compared with the entered password hash. Failed attempts are recorded, and the user is locked for one minute after three failed attempts. It includes authentication, security, Optional, and custom exception handling.

Transfer

The AccountService.transfer() method checks the transfer amount and account status. It updates the balance of both accounts and applies the overdraft rules when necessary. It also records one outgoing transaction for the sender and one incoming transaction for the receiver.

Daily Usage Reset

The current date is compared with the stored date by DebitCardService.resetDailyUsageIfNeeded(). When the date changes, all daily usage figures are reset and the new date is stored.

Transaction Filtering

The filterTransactionsByDate() method of TransactionService returns the transactions of one account for a defined period of time between a start and end date. It works for calendar and rolling periods.

ERD Diagram



Project Planning

Trello Board – User Stories and Project Planning

Trello was used to record user stories and development tasks.

How to Run

Clone or download the repository.

Launch the project in IntelliJ IDEA.

Set the Project SDK to Java 17.

Run BankApplication.Main.

Follow the command-line menu.

Testing

The project has been tested manually from the command line and by using JUnit 5. Testing covered:

User login security and account locking.

Unique Customer, User, and Account IDs.

Creating a customer and applying for an account.

Depositing, withdrawing, and transferring.

Invalid and boundary amounts.

Daily card limits.

Overdraft fees, deactivation, and reactivation.

Transaction history and account statements.

Date filtering.

Approval and rejection of card upgrades.

Data persistence after restarting the application.

Known Issues and Future Improvements

Make errors in menu number input recoverable when using Scanner.nextInt().

Use a relational database instead of text-based files.

Create standard 16-digit card numbers and calculate expiry dates dynamically.

Add a feature to reset the password.

Increase automated unit and integration testing.

Develop a graphical or web application.

Use a more robust globally unique transaction ID mechanism for large systems.