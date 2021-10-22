package com.backbase.lookup.business_structure.usecase

import android.content.Context
import com.backbase.android.flow.models.Action
import com.backbase.android.flow.v2.contracts.FlowClientContract
import com.backbase.android.flow.v2.models.InteractionResponse
import com.backbase.android.flow.v2.throwExceptionIfErrorOrNull
import com.backbase.lookup.business_structure.BusinessStructureConfiguration
import com.backbase.lookup.business_structure.module.BusinessStructureResponseModel
import com.backbase.lookup.business_structure.module.BusinessStructureTypeModel
import com.backbase.lookup.business_structure.module.GetBusinessStructureModel
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


private const val JOURNEY_NAME = "smeo_business_structure"

class BusinessStructureUsecaseImpl(
    private val context: Context,
    private val flowClient: FlowClientContract,
    private val configuration: BusinessStructureConfiguration
) : BusinessStructureUsecase {

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

    override suspend fun createCase() =
        configuration.createCaseActionName?.let {
            val responseType: Type =
                object : TypeToken<Map<String, Any?>?>() {}.type
            flowClient.performInteraction<Map<String, Any?>?>(
                Action(it, null),
                responseType
            ).throwExceptionIfErrorOrNull()
        }

    //TODO Collection APIs not supported yet - Below code to be removed/replaced
    override suspend fun requestBusinessStructures() = service.requestBusinessStructures().items

    override suspend fun submitSelectedBusinessStructure(
        type: String,
        subType: String?
    ): InteractionResponse<BusinessStructureResponseModel?>? {
        val responseType: Type =
            object : TypeToken<BusinessStructureResponseModel?>() {}.type
        return flowClient.performInteraction<BusinessStructureResponseModel?>(
            Action(
                configuration.submitBusinessStructureActionName,
                BusinessStructureTypeModel(type, subType)
            ),
            responseType
        ).throwExceptionIfErrorOrNull()
    }


    //TODO Collection APIs not supported yet - Below code to be removed/replaced
    interface Api {
        @GET("client-api/collections/v2/collections/business-structures?count=true")
        @Headers("Cache-control: no-cache")
        suspend fun requestBusinessStructures(): GetBusinessStructureModel
    }

}
