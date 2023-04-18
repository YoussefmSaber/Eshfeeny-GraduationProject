package com.example.data.remote

import com.example.domain.entity.product.ProductResponse
import com.example.domain.entity.product.ProductResponseItem
import com.example.domain.entity.patchRequestVar.PatchProductId
import com.example.domain.entity.patchresponse.PatchRequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProductApiService {

    @GET("products/category/{medicines}")
    suspend fun getMedicineFromRemote(
        @Path("medicines")
        medicines: String
    ): Response<ProductResponse>

    @GET("products/category/امساك")
    suspend fun getMedicineFromEmsaak(): ProductResponse

    @GET("products/category/الكحة")
    suspend fun getMedicineFromKo7aa(): ProductResponse

    @GET("products/category/المغص")
    suspend fun getMedicineFromM8aas(): ProductResponse

    @GET("products/{id}")
    suspend fun getMedicineDetailsFromRemote(
        @Path("id")
        id: String
    ): Response<ProductResponseItem>

    @GET("products/type/الأدوية")
    suspend fun getMedicineFromAllMedicines(): ProductResponse

    @PATCH("users/{id}/favorites")
    suspend fun addMedicneToFavorite(
        @Path("id")
        id: String,
        @Body
        productId: PatchProductId
    ): PatchRequestResponse

    @GET("products/user/{userId}/favorites")
    suspend fun getFavoriteProducts (
        @Path("userId")
        id: String
    ): ProductResponse

    @DELETE("users/{userId}/favorites/{productId}")
    suspend fun deleteFavoriteProduct(
        @Path("userId")
        userId: String,
        @Path("productId")
        productId: String
    ): PatchRequestResponse
}