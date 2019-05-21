# **Transaction**
--------

**Get all transactions**
----
  Returns JSON data object including all transactions in the database.

* **URL**

  /transactions/getAll

* **Method:**

  `GET`

*  **URL Params**

   None

* **Success Response**

  * **Code:** 200 (OK)
    **Content:** `{ "accountTo" : 1, "accountFrom":2, "amount" : 10.0, "currency":"USD"},
    { "accountTo" : 2, "accountFrom":3, "amount" : 50.0, "currency":"EUR"}`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

  ```
  curl -X GET http://localhost:8080/transactions/getAll
  ```

---

  **Get transaction by id**
----
  Returns transaction by transaction id.

* **URL**

  /transactions/:id

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `id=[string]`

* **Success Response:**

  * **Code:** 200 (OK)
    **Content:** `{ "accountTo" : 1, "accountFrom":2, "amount" : 10.0, "currency":"USD"}`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

```
curl -X GET http://localhost:8080/transactions/uuid_string
```

* **Notes:**

  For security reasons, the transactions' ids are in [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier) format. In order to get the transaction id, you must have access to the server.

---

  **Make transaction**
----

  Makes transaction between two accounts.

  * **URL**

    /transactions/makeTransaction

  * **Method:**

    `POST`

  *  **URL Params**

     None

  * **Data Params**

     None


  * **Success Response**

    * **Code:** 201 (CREATED)
      **Content:** `{ "accountTo" : 1, "accountFrom":2, "amount" : 10.0, "currency":"USD"}``

  * **Error Response:**

    * **Code:** 404 (NOT FOUND)
      * Accounts not found.

    * **Code:** 400 (BAD REQUEST)
      * Insufficient amount of money from account _accountFrom_.


  * **Sample Call:**

    ```
    curl -X POST -H "Content-Type: application/json" -d '{"accountTo":1, "accountFrom":2, "amount":10.0, "currency":"USD"}' http://localhost:8080/transactions/makeTransaction
    ```

  ---
