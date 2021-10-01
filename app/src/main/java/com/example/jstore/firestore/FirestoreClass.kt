package com.example.jstore.firestore

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.data.source.local.Prefs.productId
import com.example.jstore.models.*
import com.example.jstore.utils.Constants
import com.example.jstore.utils.Constants.ADMIN
import com.example.jstore.utils.Constants.CARTS
import com.example.jstore.utils.Constants.CART_ID
import com.example.jstore.utils.Constants.CATEGORY
import com.example.jstore.utils.Constants.CATEGORY_ID
import com.example.jstore.utils.Constants.CHECKED_OUT
import com.example.jstore.utils.Constants.EMAIL_ADMIN
import com.example.jstore.utils.Constants.IMAGE
import com.example.jstore.utils.Constants.JASAPENGIRIMAN_ID
import com.example.jstore.utils.Constants.JASA_PENGIRIMAN
import com.example.jstore.utils.Constants.LOKASIPENGIRIMAN_ID
import com.example.jstore.utils.Constants.LOKASI_PENGIRIMAN
import com.example.jstore.utils.Constants.METODEPEMBAYARAN_ID
import com.example.jstore.utils.Constants.METODE_PEMBAYARAN
import com.example.jstore.utils.Constants.PASSWORD_ADMIN
import com.example.jstore.utils.Constants.PRODUCTS
import com.example.jstore.utils.Constants.PRODUCT_ID
import com.example.jstore.utils.Constants.REKENING
import com.example.jstore.utils.Constants.REKENING_ID
import com.example.jstore.utils.Constants.USERS
import com.example.jstore.utils.Constants.USER_ID
import com.example.jstore.utils.logDebug
import com.example.jstore.utils.logError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(
        userInfo: User,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }
    fun updateProduct(
        title: String, price: String, description: String, stockQuantity: Int, category: String, imageUrl: String,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(PRODUCTS)
            .document(productId)
            .update("title", title, "price", price,
            "description", description, "stockQuantity", stockQuantity, "category", category)
            .addOnSuccessListener {
            uploadImageProductToFirestore(imageUrl.toUri(), onSuccessListener = { imageUrl ->
                        updateProductImageUrl(productId, imageUrl)
                    }, onFailureListener = {})
                    onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }

    fun updateProfileUser(
        user: User,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(USERS)
            .document(Prefs.userId)
            .set(user)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }

    }

    fun uploadImageToFirestore(
        fileUri: Uri,
        onSuccessListener: (imageUrl: String) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    updateProfileImageUrl(imageUrl = imageUrl, onSuccessListener = {
                        onSuccessListener(imageUrl)
                    }, onFailureListener = { e ->
                        onFailureListener(e)
                    })
                }
            }
            .addOnFailureListener {
                onFailureListener(it)
            }
    }

    private fun updateProfileImageUrl(
        imageUrl: String,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(USERS)
            .document(Prefs.userId)
            .update("image", imageUrl)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }

    fun loginAdmin(
        email: String,
        password: String,
        onLoginSuccess: (admin: Admin) -> Unit,
        onLoginFailed: (e: String) -> Unit
    ) {
        mFirestore.collection(ADMIN)
            .whereEqualTo(EMAIL_ADMIN, email)
            .whereEqualTo(PASSWORD_ADMIN, password)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result
                    if (result?.isEmpty == true) {
                        onLoginFailed("Email atau password salah!")
                    } else {
                        onLoginSuccess(result?.first()?.toObject(Admin::class.java) ?: Admin())
                    }
                } else {
                    onLoginFailed(it.exception?.message.toString())
                    logError(it.exception?.message.toString())
                }
            }
            .addOnFailureListener {
                onLoginFailed(it.message.toString())
                logError(it.message.toString())
            }
    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: ""
    }

    fun getUserDetails(
        onSuccessListener: (user: User) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {
                    Prefs.userId = user.id
                    Prefs.userFullName = user.firstName +" "+ user.lastName
                    onSuccessListener(user)
                }

            }.addOnFailureListener { exception ->
                onFailureListener(exception)
            }
    }

    fun subscribeUserProfile(
        onSuccessListener: (user: User) -> Unit,
        onFailureListener: (e: String) -> Unit
    ) {
        mFirestore.collection(USERS)
            .document(getCurrentUserId())
            .addSnapshotListener { value, error ->
                error?.let { e ->
                    onFailureListener(e.message.toString())
                }
                value?.let { documentSnapshot ->
                    val user = documentSnapshot.toObject<User>()
                    if (user != null) {
                        Prefs.userId = user.id
                        Prefs.userFullName = user.firstName +" "+ user.lastName
                        onSuccessListener(user)
                    }
                }
            }
    }

    fun getAdminDetails(
        onSuccessListener: (admin: Admin) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(ADMIN)
            .document(Prefs.adminId)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getAdminDetails: $document")
                val admin = document.toObject(Admin::class.java)!!
                Prefs.adminId = admin.idAdmin
                Prefs.adminFullName = admin.fullNameAdmin
                onSuccessListener(admin)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getAdminDetails: ${e.message}")
            }
    }

    fun updateProfileAdmin(
        fullnameAdmin: String, emailAdmin: String, mobileAdmin: String,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(ADMIN)
            .document(Prefs.adminId)
            .update(
                "fullNameAdmin", fullnameAdmin,
                "emailAdmin", emailAdmin,
                "mobileAdmin", mobileAdmin
            )
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }

    fun subscribeAdminProfile(
        onSuccessListener: (admin: Admin) -> Unit,
        onFailureListener: (e: String) -> Unit
    ) {
        mFirestore.collection(ADMIN)
            .document(Prefs.adminId)
            .addSnapshotListener { value, error ->
                error?.let { e ->
                    onFailureListener(e.message.toString())
                }
                value?.let { documentSnapshot ->
                    val admin = documentSnapshot.toObject<Admin>()
                    if (admin != null) {
                        Prefs.adminId = admin.idAdmin
                        Prefs.adminFullName = admin.fullNameAdmin
                        onSuccessListener(admin)
                    }
                }
            }
    }

    fun uploadImageAdminToFirestore(
        fileUri: Uri,
        onSuccessListener: (imageUrl: String) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    updateProfileAdminImageUrl(imageUrl = imageUrl, onSuccessListener = {
                        onSuccessListener(imageUrl)
                    }, onFailureListener = { e ->
                        onFailureListener(e)
                    })
                }
            }
            .addOnFailureListener {
                onFailureListener(it)
            }
    }

    private fun updateProfileAdminImageUrl(
        imageUrl: String,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(ADMIN)
            .document(Prefs.adminId)
            .update("imageAdmin", imageUrl)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }


    fun getProductList(
        onSuccessListener: (products: List<Product>) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(PRODUCTS)
            .addSnapshotListener { value, error ->
                value?.let { document ->
                    logDebug("getDashboardItemsList: ${document.documents}")
                    val productList = document.documents.map {
                        it.toObject(Product::class.java) ?: Product()
                    }
                    onSuccessListener(productList)
                }

                error?.let {
                    logError("getDashboardItemsList: ${it.message}")
                    onFailureListener(Exception(it))
                }
            }
    }

    fun getCustomerList(
        onSuccessListener: (customers: List<User>) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(USERS)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getCustomerItemsList: ${document.documents}")

                val customerList = document.documents.map {
                    it.toObject(User::class.java) ?: User()
                }
                onSuccessListener(customerList)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getCustomerItemsList: ${e.message}")
            }
    }

    private fun uploadImageProductToFirestore(
        fileUri: Uri,
        onSuccessListener: (imageUrl: String) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    onSuccessListener(imageUrl)
                }
            }
            .addOnFailureListener {
                onFailureListener(it)
            }
    }

    private fun updateProductImageUrl(productId: String, imageUrl: String) {
        mFirestore.collection(PRODUCTS)
            .document(productId)
            .update(IMAGE, imageUrl)
    }

    fun addProduct(
        product: Product,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(PRODUCTS)
            .add(product)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document != null) {
                        val id = document.id
                        updateId(PRODUCTS, id, PRODUCT_ID, onSuccessListener = {}, onFailureListener = {})
                        uploadImageProductToFirestore(product.image.toUri(), onSuccessListener = { imageUrl ->
                            updateProductImageUrl(id, imageUrl)
                        }, onFailureListener = {})
                        onSuccessListener()
                    }
                }
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }

    }

    fun addRekening(
        rekening: Rekening,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(REKENING)
            .add(rekening)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document != null) {
                        val id = document.id
                        updateId(REKENING, id, REKENING_ID, onSuccessListener = {}, onFailureListener = {})
                        onSuccessListener()
                    }
                }
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }

    }

    fun getRekeningList(
        onSuccessListener: (rekening: List<Rekening>) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(REKENING)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getRekeningItemsList: ${document.documents}")

                val rekeningList = document.documents.map {
                    it.toObject(Rekening::class.java) ?: Rekening()
                }
                onSuccessListener(rekeningList)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getRekeningItemsList: ${e.message}")
            }
    }

    fun addJasaPengiriman(
        jasaPengiriman: JasaPengiriman,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(JASA_PENGIRIMAN)
            .add(jasaPengiriman)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document != null) {
                        val id = document.id
                        updateId(JASA_PENGIRIMAN, id, JASAPENGIRIMAN_ID, onSuccessListener = {}, onFailureListener = {})
                        onSuccessListener()
                    }
                }
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }

    }

    fun getJasaPengirimanList(
        onSuccessListener: (jasaPengiriman: List<JasaPengiriman>) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(JASA_PENGIRIMAN)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getJasaPengirimanItemsList: ${document.documents}")

                val jasaPengirimanList = document.documents.map {
                    it.toObject(JasaPengiriman::class.java) ?: JasaPengiriman()
                }
                onSuccessListener(jasaPengirimanList)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getRekeningItemsList: ${e.message}")
            }
    }

    fun addLokasiPengiriman(
        lokasiPengiriman: LokasiPengiriman,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(LOKASI_PENGIRIMAN)
            .add(lokasiPengiriman)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document != null) {
                        val id = document.id
                        updateId(LOKASI_PENGIRIMAN, id, LOKASIPENGIRIMAN_ID, onSuccessListener = {}, onFailureListener = {})
                        onSuccessListener()
                    }
                }
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }

    }

    fun getLokasiPengirimanList(
        onSuccessListener: (lokasiPengiriman: List<LokasiPengiriman>) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(LOKASI_PENGIRIMAN)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getLokasiPengirimanItemsList: ${document.documents}")

                val lokasiPengirimanList = document.documents.map {
                    it.toObject(LokasiPengiriman::class.java) ?: LokasiPengiriman()
                }
                onSuccessListener(lokasiPengirimanList)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getRekeningItemsList: ${e.message}")
            }
    }

    fun addMetodePembayaran(
        metodePembayaran: MetodePembayaran,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(METODE_PEMBAYARAN)
            .add(metodePembayaran)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document != null) {
                        val id = document.id
                        updateId(METODE_PEMBAYARAN, id, METODEPEMBAYARAN_ID, onSuccessListener = {}, onFailureListener = {})
                        onSuccessListener()
                    }
                }
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }

    }

    fun getMetodePembayaranList(
        onSuccessListener: (metodePembayaran: List<MetodePembayaran>) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(METODE_PEMBAYARAN)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getMetodePembayaranItemsList: ${document.documents}")

                val metodePembayaranList = document.documents.map {
                    it.toObject(MetodePembayaran::class.java) ?: MetodePembayaran()
                }
                onSuccessListener(metodePembayaranList)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getMetodePembayaranList: ${e.message}")
            }
    }

    fun addCategory(
        category: Category,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(CATEGORY)
            .add(category)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document != null) {
                        val id = document.id
                        updateId(CATEGORY, id, CATEGORY_ID, onSuccessListener = {}, onFailureListener = {})
                        onSuccessListener()
                    }
                }
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }

    }

    fun getCategoryList(
        onSuccessListener: (category: List<Category>) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(CATEGORY)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getCategoryItemsList: ${document.documents}")

                val categoryList = document.documents.map {
                    it.toObject(Category::class.java) ?: Category()
                }
                onSuccessListener(categoryList)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getCategoryList: ${e.message}")
            }
    }

    fun subscribeToCart(
        onSuccessListener: (cart: Cart) -> Unit,
        onFailureListener: (e: String) -> Unit
    ) {
        mFirestore.collection(CARTS)
            .whereEqualTo(USER_ID, Prefs.userId)
            .whereEqualTo(CHECKED_OUT, false)
            .addSnapshotListener { value, error ->
                error?.let {
                    onFailureListener(it.message.toString())
                }
                value?.let { document ->
                    logDebug("userId: ${Prefs.userId}, document: ${document.size()}")
                    if (document.documents.isEmpty()) {
                        createActiveCart(onSuccessListener = {}, onFailureListener = {})
                    }
                    val cart = document.documents.map {
                        it.toObject<Cart>() ?: Cart()
                    }
                    if (cart.isNotEmpty()) {
                        onSuccessListener(cart.first())
                    }
                }
            }
    }

    private fun getMyCart(
        onSuccessListener: (cart: Cart) -> Unit
    ) {
        mFirestore.collection(CARTS)
            .whereEqualTo(USER_ID, Prefs.userId)
            .whereEqualTo(CHECKED_OUT, false)
            .get()
            .addOnSuccessListener { document ->
                val cart = document.documents.map {
                    it.toObject<Cart>() ?: Cart()
                }
                if (cart.isNotEmpty()) onSuccessListener(cart.first())
            }
    }

    fun addProductToCart(
        product: Product,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        getMyCart { cart ->
            if (cart.products.any { it.productId == product.productId }) {
                updateMyCart(cartId = cart.cartId,
                    products = cart.products.toMutableList(),
                    product = product,
                    onSuccessListener = { onSuccessListener() },
                    onFailureListener = { onFailureListener(it) })
            } else {
                mFirestore.collection(CARTS)
                    .document(Prefs.activeCartId)
                    .update(PRODUCTS, FieldValue.arrayUnion(product))
                    .addOnSuccessListener {
                        onSuccessListener()
                    }
                    .addOnFailureListener {
                        onFailureListener(it)
                        logError("addProductToCart: ${it.message}")
                    }
            }
        }
    }

    fun updateMyCart(
        cartId: String,
        products: MutableList<Product>,
        product: Product,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        try {
            var itemIndex = 0
            products.forEachIndexed { index, p ->
                if (p.productId == product.productId) {
                    itemIndex = index
                    return@forEachIndexed
                }
            }
            logDebug("checkIndex: $itemIndex")
            products[itemIndex] = product
            mFirestore.collection(CARTS)
                .document(cartId)
                .update(PRODUCTS, products)
                .addOnSuccessListener {
                    onSuccessListener()
                }
                .addOnFailureListener {
                    onFailureListener(it)
                    logError("updateMyCart: ${it.message}")
                }
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }

    fun removeProductFromCart(
        product: Product,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(CARTS)
            .document(Prefs.activeCartId)
            .update(PRODUCTS, FieldValue.arrayRemove(product))
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener {
                onFailureListener(it)
                logError("removeProductFromCart: ${it.message}")
            }
    }

    private fun createActiveCart(onSuccessListener: () -> Unit, onFailureListener: (e: Exception) -> Unit) {
        val cart = Cart(
            userId = Prefs.userId
        )
        mFirestore.collection(CARTS)
            .add(cart)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document != null) {
                        val id = document.id
                        updateId(CARTS, id, CART_ID, onSuccessListener = {}, onFailureListener = {})
                        onSuccessListener()
                    }
                }
            }
            .addOnFailureListener { onFailureListener(it) }
    }

    private fun updateId(collection: String, id: String, documentName: String, onSuccessListener: () -> Unit, onFailureListener: (e: Exception) -> Unit) {
        mFirestore.collection(collection)
            .document(id)
            .update(documentName, id)
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener { onFailureListener(it) }
    }

    fun deleteProduct( productID: String, onSuccessListener: () -> Unit, onFailureListener: (e: Exception) -> Unit) {
        mFirestore.collection(PRODUCTS)
            .document(productID)
            .delete()
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener {
                onFailureListener(it)
                logError("removeDelete: ${it.message}")
            }
    }

}