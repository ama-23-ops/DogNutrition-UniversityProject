package com.example.dognutrionapp.ui.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.database.Cursor
import com.example.dognutrionapp.ui.models.Product
import com.example.dognutrionapp.ui.models.User

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "DogNutritionApp.db"
        const val DATABASE_VERSION = 2

        // User Table
        const val TABLE_USER = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "username"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_IS_ADMIN = "is_admin"

        // Product Table
        const val TABLE_PRODUCTS = "products"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_PRODUCT_NAME = "name"
        const val COLUMN_PRODUCT_DESCRIPTION = "description"
        const val COLUMN_PRODUCT_PRICE = "price"
        const val COLUMN_PRODUCT_RATING = "rating"
        const val COLUMN_PRODUCT_IMAGE = "image"

        // Cart Table
        const val TABLE_CART = "cart"
        const val COLUMN_CART_ID = "cart_id"
        const val COLUMN_CART_PRODUCT_ID = "product_id"
        const val COLUMN_CART_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create User Table
        val createUserTable = ("CREATE TABLE $TABLE_USER (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USER_NAME TEXT UNIQUE, " +
                "$COLUMN_USER_PASSWORD TEXT, " +
                "$COLUMN_USER_IS_ADMIN INTEGER DEFAULT 0)") // 0 = regular user, 1 = admin

        // Create Product Table
        val createProductTable = ("CREATE TABLE $TABLE_PRODUCTS (" +
                "$COLUMN_PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_PRODUCT_NAME TEXT, " +
                "$COLUMN_PRODUCT_DESCRIPTION TEXT, " +
                "$COLUMN_PRODUCT_PRICE REAL, " +
                "$COLUMN_PRODUCT_RATING REAL, " +
                "$COLUMN_PRODUCT_IMAGE TEXT)")

        // Create Cart Table
        val createCartTable = ("CREATE TABLE $TABLE_CART (" +
                "$COLUMN_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_CART_PRODUCT_ID INTEGER, " +
                "$COLUMN_CART_QUANTITY INTEGER, " +
                "FOREIGN KEY($COLUMN_CART_PRODUCT_ID) REFERENCES $TABLE_PRODUCTS($COLUMN_PRODUCT_ID))")

        db.execSQL(createUserTable)
        db.execSQL(createProductTable)
        db.execSQL(createCartTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
            onCreate(db)
        }
    }

    // Add a user
    fun addUser(email: String, name: String, password: String, isAdmin: Boolean): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, name)
            put(COLUMN_USER_PASSWORD, password)
            put(COLUMN_USER_IS_ADMIN, if (isAdmin) 1 else 0)
        }
        val result = db.insert(TABLE_USER, null, values)
        db.close()
        return result != -1L
    }

    // Check user credentials
    fun checkUser(email: String, password: String): Pair<Boolean, Boolean> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT $COLUMN_USER_IS_ADMIN FROM $TABLE_USER WHERE $COLUMN_USER_NAME = ? AND $COLUMN_USER_PASSWORD = ?",
            arrayOf(email, password)
        )
        val isValidUser = cursor.moveToFirst()
        val isAdmin = if (isValidUser) cursor.getInt(0) == 1 else false
        cursor.close()
        db.close()
        return Pair(isValidUser, isAdmin)
    }

    // Check if email exists
    fun getUserByEmail(email: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT $COLUMN_USER_NAME FROM $TABLE_USER WHERE $COLUMN_USER_NAME = ?",
            arrayOf(email)
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    // Fetch all products
    fun getProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)

        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_RATING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE))
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productList
    }

    // Add product
    fun addProduct(product: Product): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PRODUCT_NAME, product.name)
            put(COLUMN_PRODUCT_DESCRIPTION, product.description)
            put(COLUMN_PRODUCT_PRICE, product.price)
            put(COLUMN_PRODUCT_RATING, product.rating)
            put(COLUMN_PRODUCT_IMAGE, product.imageUrl)
        }
        val result = db.insert(TABLE_PRODUCTS, null, values)
        db.close()
        return result != -1L
    }

    // Update product
    fun updateProduct(product: Product): Boolean {
        val db = writableDatabase
        val productId = product.id
        val values = ContentValues().apply {
            put(COLUMN_PRODUCT_NAME, product.name)
            put(COLUMN_PRODUCT_DESCRIPTION, product.description)
            put(COLUMN_PRODUCT_PRICE, product.price)
            put(COLUMN_PRODUCT_RATING, product.rating)
            put(COLUMN_PRODUCT_IMAGE, product.imageUrl)
        }
        val result = db.update(
            TABLE_PRODUCTS,
            values,
            "$COLUMN_PRODUCT_ID = ?",
            arrayOf(product.id.toString())
        )
        db.close()
        return result > 0
    }

    // Delete product
    fun deleteProduct(productId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_PRODUCTS,
            "$COLUMN_PRODUCT_ID = ?",
            arrayOf(productId.toString())
        )
        db.close()
        return result > 0
    }

    // Update quantity of a product in the cart
    fun updateCartQuantity(productId: Int, quantity: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CART_QUANTITY, quantity)
        }
        val result = db.update(
            TABLE_CART,
            values,
            "$COLUMN_CART_PRODUCT_ID = ?",
            arrayOf(productId.toString())
        )
        db.close()
        return result > 0
    }

    // Get all users
    fun getAllUsers(): List<User> {
        val userList = mutableListOf<User>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USER", null) // Select all columns

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
                val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME))
                val isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_IS_ADMIN)) == 1

                userList.add(User(id, username, "", address = "", isAdmin)) // Create User objects
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    fun insertUserProfile(user: User): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("name", user.name) //add user.name
        contentValues.put("email", user.email)
        contentValues.put("address", user.address)
        contentValues.put("isAdmin", user.isAdmin)



        val result = db.insert("user_profile", null, contentValues)
        db.close()
        return result != -1L
    }

    fun getUserProfile(): User? {
        val db = this.readableDatabase
        // Include ID, email, and isAdmin in the query
        val cursor = db.rawQuery("SELECT id, name, email, address, isAdmin FROM user_profile", null)

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id")) // Get id as Int
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
            val isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1

            val user = User(id = id, email = email, password = "", name = name, address = address, isAdmin = isAdmin) // Pass all parameters, including id
            cursor.close()
            user

        } else {
            cursor.close()
            null
        }
    }

    // Get a single product by ID (add this back!)
    fun getProductById(productId: Int): Product? {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_PRODUCT_ID = ?",
            arrayOf(productId.toString())  // Make sure productId is used correctly
        )

        var product: Product? = null
        if (cursor.moveToFirst()) {
            product = Product(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_RATING)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE)) // Make sure to get String
            )
        }
        cursor.close()
        db.close()
        return product
    }

    // Add a product to the cart (simplified)
    fun addToCart(productId: Int, quantity: Int): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val contentValues = ContentValues().apply {
            put("product_id", productId)
            put("quantity", quantity)
        }

        // Assuming there's a "cart" table
        val result = db.insert("cart", null, contentValues)
        db.close()

        return result != -1L
    }
}
