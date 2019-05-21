# **Account**
--------

**Get all accounts**
----
  Returns JSON data object including all accounts in the database.

* **URL**

  /accounts/getAll

* **Method:**

  `GET`

*  **URL Params**

   None

* **Data Params**

   None

* **Success Response**

  * **Code:** 200 (OK)
    **Content:** `{ "username" : "name1@gmail.com", "password" : "surname1", "balance" : 10000.0, "currency":"USD"},
    { "username" : "name2", "password" : "surname2", "balance" : 20000.0, "currency":"EUR"}`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

  ```
  curl -X GET http://localhost:8080/accounts/getAll
  ```

---

  **Get account by id**
----
  Returns account by account id.

* **URL**

  /accounts/:id

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `id=[integer]`

* **Success Response:**

  * **Code:** 200 (OK)
    **Content:** `{ "username" : "name1@gmail.com", "password" : "surname1", "balance" : 10000.0, "currency":"USD"}`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

```
curl -X GET http://localhost:8080/accounts/1
```

---

  **Get account by username**
----
  Returns account by username.

* **URL**

  /accounts/uname/:username

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `username=[string]`

* **Success Response:**

  * **Code:** 200 (OK)
    **Content:** `{ "username" : "name1@gmail.com", "password" : "surname1", "balance" : 10000.0, "currency":"EUR"}`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

```
curl -X GET http://localhost:8080/accounts/name1@gmail.com
```


**Create account**
----
  Creates an account and stores them to the database.

* **URL**

  /accounts/createAccount

* **Method:**

  `POST`

* **Success Response:**

  * **Code:** 201 (CREATED)
    **Content:** `"Account created successfully!"`


* **Error Response:**

  * **Code:** 400 (BAD REQUEST)


* **Sample Call:**

```  
curl -X POST -H "Content-Type: application/json" -d '{"username":"name6", "password":"surname6", "balance":60000.0, "currency":"EUR"}' http://localhost:8080/accounts/createAccount
```

* **Notes:**

  In order to be able to create an account, the account username must match a corresponding user's email.

---

**Delete account**
----
  Deletes an account from the database.

* **URL**

  /accounts/:id

* **Method:**

  `DELETE`

* **Success Response:**

  * **Code:** 204 (NO CONTENT)
    **Content:** `"Account deleted successfully!"`


* **Error Response:**

  * **Code:** 400 (BAD REQUEST)


* **Sample Call:**

```  
curl -X DELETE http://localhost:8080/accounts/1
```

---

**Update account**
----
  Updates an account and stores the new data to the database.

* **URL**

  /accounts/:id

* **Method:**

  `PUT`

* **Success Response:**

  * **Code:** 204 (NO CONTENT)
    **Content:** `"Account updated successfully!"`


* **Error Response:**

  * **Code:** 400 (BAD REQUEST)


* **Sample Call:**

```  
curl -X PUT -H "Content-Type: application/json" -d '{"username":"newName", "password":"newSurname", "balance":12345.0, "currency":"EUR"}' http://localhost:8080/accounts/1
```
