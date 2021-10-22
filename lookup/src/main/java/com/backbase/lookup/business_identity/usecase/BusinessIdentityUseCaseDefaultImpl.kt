package com.backbase.lookup.business_identity.usecase

import android.content.Context
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.android.flow.v2.throwExceptionIfErrorOrNull
import com.backbase.lookup.business_identity.BusinessIdentityConfiguration
import com.backbase.lookup.business_identity.models.BusinessDetailsModel
import com.backbase.lookup.business_identity.models.IdentityModel
import com.backbase.lookup.business_identity.models.Industry
import com.backbase.lookup.business_identity.models.IndustryCollectionResponseModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.lang.reflect.Type
import java.text.DateFormat

private const val JOURNEY_NAME = "sme-onboarding-identity"

class BusinessIdentityUseCaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: BusinessIdentityConfiguration
) : BusinessIdentityUseCase {

    //TODO Collection APIs not supported yet - Below code to be removed/replaced
    private var service: Api

    //TODO Collection APIs not supported yet - Below code to be removed/replaced
    init {
        val gson: Gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.LONG)
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("${flowClient.baseURI.toString()}/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        service = retrofit.create(Api::class.java)
    }

    override suspend fun submitBusinessIdentity(industry: Industry, businessDescription: String?, companyWebsite: String?): InteractionResponse<BusinessDetailsModel?>?{

        val requestModel = IdentityModel(
            businessDescription,
            arrayListOf(industry),
            companyWebsite
        )

        val responseType: Type =
            object : TypeToken<BusinessDetailsModel?>() {}.type

        return flowClient.performInteraction<BusinessDetailsModel?>(
            Action(
                configuration.submitBusinessIdentityActionName,
                requestModel
            ),
            responseType
        ).throwExceptionIfErrorOrNull()
    }

    override suspend fun requestIndustries() = service.requestIndustries().items

    //TODO Collection APIs not supported yet - Below code to be removed/replaced
    interface Api {
        @GET("client-api/collections/v2/collections/industries?count=true")
        @Headers("Cache-control: no-cache")
        suspend fun requestIndustries(): IndustryCollectionResponseModel
    }
}