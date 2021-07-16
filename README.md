
# JCapTrack

## A Java ACB and Capital Gains Calculator

### Project Proposal

With the rise of fully online digital brokerages, access to trading in the equity markets has never been easier. Research shows that the Covid-19 pandemic has had a profound impact on the number of retail investors entering the stock market for the first time.  This flurry of new activity means that there is a demand for ways to track portfolio trading and performance, as well as important metrics for when it comes time to file taxes. 

Individual investment brokerages often track this information, but they do not share it between banks.  As a result, someone who is trading at multiple brokerages, or who has switched brokerages, will not have a clear consolidated financial picture.  Even worse, the details of information across brokerages such as the *adjusted cost base* (ACB) of a traded equity may not be correctly indicated in brokerage statements.  This could result in improper tax filing information leading to auditing and penalties from the tax authority.

**JTradeTracker** aims to allow a retail investor to enter and consolidate their trades across all accounts.  Features that should be included at a minimum:

- Recording of transactions
  - Date of transaction
  - Trading account  
  - Buy/Sell
  - Name of equity
  - Transaction amount
  - USD/CAD
  - Number of shares
  - Commission
  - Comments  
     
    
- Account information
  - Brokerage
  - Registered/Unregistered
  - Account holdings
    

- Personal portfolio
  - Consolidated holdings across all accounts
  - Search for transactions
  - Tax information
    - Recording of capital gains
    - Tax year summary for reporting
    
**Wish List**
- Import/Export

This project is of interest to me because every year when I file my taxes I have to sift through piles of statements.  I would prefer to have an automatically generated report at the end of the year.  Canadian Brokerages do not provide good information on capital gains because of the tax structure in Canada.  The structure of the project is well suited for OOP like Java. 

## User Stories

- As a user, I want to be able to add multiple transactions to my personal portfolio.
- As a user, I want my USD trades to be converted to CAD by recording the exchange rate at the time of the transaction.  
- As a user, I want to have my capital gains automatically tracked and recorded when disposing of a security.  (This means comparing the average purchase price to the sell price in CAD).
- As a user, I want to be able to search for transactions by security name.





