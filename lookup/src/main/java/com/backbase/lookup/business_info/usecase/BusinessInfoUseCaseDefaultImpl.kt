package com.backbase.lookup.business_info.usecase

import android.content.Context
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.android.flow.v2.throwExceptionIfErrorOrNull
import com.backbase.lookup.business_info.BusinessInfoConfiguration
import com.backbase.lookup.business_info.models.BusinessDetailsModel
import com.backbase.lookup.business_info.models.BusinessStructureInfo
import com.backbase.lookup.business_info.models.GetBusinessStructureModel
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

private const val JOURNEY_NAME = "sme-onboarding_info"

class BusinessInfoUseCaseDefaultImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: BusinessInfoConfiguration
) : BusinessInfoUseCase {

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

    override suspend fun submitBusinessDetails(
        type: String,
        subType: String?,
        legalName: String,
        knownName: String,
        ein: Int?,
        establishedDate: String,
        operationState: String
    ) : InteractionResponse<Map<String, Any?>?>? {

        val formData = BusinessDetailsModel(
            BusinessStructureInfo(type, subType),
            establishedDate,
            knownName,
            ein,
            legalName,
            operationState
        )

        val responseType: Type =
            object : TypeToken<Map<String, Any?>?>() {}.type
        return flowClient.performInteraction<Map<String, Any?>?>(
            Action(
                configuration.submitBusinessDetailsAction,
                formData
            ),
            responseType
        ).throwExceptionIfErrorOrNull()

    }

    //TODO Collection APIs not supported yet - Below code to be removed/replaced
    override suspend fun requestBusinessStructures() = service.requestBusinessStructures().items

    //TODO Collection APIs not supported yet - Below code to be removed/replaced
    interface Api {
        @GET("client-api/collections/v2/collections/business-structures?count=true")
        @Headers("Cache-control: no-cache")
        suspend fun requestBusinessStructures(): GetBusinessStructureModel
    }
}