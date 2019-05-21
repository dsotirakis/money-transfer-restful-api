# **User**
--------

**Get all users**
----
  Returns JSON data object including all users in the database.

* **URL**

  /users/getAll

* **Method:**

  `GET`
  
*  **URL Params**

   None

* **Data Params**

   None

* **Success Response**

  * **Code:** 200 (OK)
    **Content:** `{ "name" : "name1", "surname" : "surname1", "email" : "name1@gmail.com" },
    { "name" : "name2", "surname" : "surname2", "email" : "name2@gmail.com" }`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

  ```
  curl -X GET http://localhost:8080/users/getAll
  ```

---

  **Get user by id**
----
  Returns user by user id.

* **URL**

  /users/:id

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `id=[integer]`

* **Success Response:**

  * **Code:** 200 (OK)
    **Content:** `{ "name" : "name1", "surname" : "surname1", "email" : "name1@gmail.com" }`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

```
curl -X GET http://localhost:8080/users/1
```

---

  **Get user by name**
----
  Returns user by name.

* **URL**

  /users/name/:name

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `name=[string]`

* **Success Response:**

  * **Code:** 200 (OK)
    **Content:** `{ "name" : "name1", "surname" : "surname1", "email" : "name1@gmail.com" }`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

```
curl -X GET http://localhost:8080/users/name1
```

---

  **Get user by surname**
----
  Returns user by surname.

* **URL**

  /users/surname/:surname

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `surname=[string]`

* **Success Response:**

  * **Code:** 200 (OK)
    **Content:** `{ "name" : "name1", "surname" : "surname1", "email" : "name1@gmail.com" }`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

```
curl -X GET http://localhost:8080/users/surname1
```

---

  **Get user by email**
----
  Returns user by email.

* **URL**

  /users/mail/:email

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `email=[string]`

* **Success Response:**

  * **Code:** 200 (OK)
    **Content:** `{ "name" : "name1", "surname" : "surname1", "email" : "name1@gmail.com" }`


* **Error Response:**

  * **Code:** 404 (NOT FOUND)


* **Sample Call:**

```
curl -X GET http://localhost:8080/users/email
```

---

**Create user**
----
  Creates a user and stores them to the database.

* **URL**

  /users/createUser

* **Method:**

  `POST`

* **Success Response:**

  * **Code:** 201 (CREATED)
    **Content:** `"User created successfully!"`


* **Error Response:**

  * **Code:** 400 (BAD REQUEST)


* **Sample Call:**

```  
curl -X POST -H "Content-Type: application/json" -d '{"name":"name6", "surname":"surname6", "email":"email6"}' http://localhost:8080/users/createUser
```

---

**Delete user**
----
  Deletes a user from the database.

* **URL**

  /users/:id

* **Method:**

  `DELETE`

* **Success Response:**

  * **Code:** 204 (NO CONTENT)
    **Content:** `"User deleted successfully!"`


* **Error Response:**

  * **Code:** 400 (BAD REQUEST)


* **Sample Call:**

```  
curl -X DELETE http://localhost:8080/users/1
```

---

**Update user**
----
  Updates a user and stores the new data to the database.

* **URL**

  /users/:id

* **Method:**

  `PUT`

* **Success Response:**

  * **Code:** 204 (NO CONTENT)
    **Content:** `"User updated successfully!"`


* **Error Response:**

  * **Code:** 400 (BAD REQUEST)


* **Sample Call:**

```  
curl -X PUT -H "Content-Type: application/json" -d '{"name":"newName", "surname":"newSurname", "email":"newEmail@gmail.com"}' http://localhost:8080/users/1
```

* **Notes:**

  Here we must specify the new details in the JSON string, and the user that these changes will apply to, at the end of the URL file.
