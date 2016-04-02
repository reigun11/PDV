package br.com.trainning.pdv_2016.domain.network;

import java.util.List;


import br.com.trainning.pdv_2016.domain.model.Compra;
import br.com.trainning.pdv_2016.domain.model.Produto;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public class APIClient {

    private static RestAdapter REST_ADAPTER;

    //.setEndpoint("http://10.0.3.2:8080/pdvserver/rest")

    private static void createAdapterIfNeeded() {

        if (REST_ADAPTER == null) {
            REST_ADAPTER = new RestAdapter.Builder()
                    .setEndpoint("http://www.qpainformatica.com.br/pdvserver/rest")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient())
                    .build();
        }
    }

    public APIClient() {
        createAdapterIfNeeded();
    }

    public RestServices getRestService() {
        return REST_ADAPTER.create(RestServices.class);
    }


    public interface RestServices {
        @GET("/produto/todos")
        void getAllProdutos(
                Callback<List<Produto>> callbackProdutos
        );

        @Headers( "Content-Type: application/json" )
        @POST("/compra/cadastro")
        void enviarCompra(
                @Body Compra compra,
                Callback<String> callbackCompra
        );

        @FormUrlEncoded()
        @POST("/produto")
        void createProduto(
                @Field("id") String codigoBarras,
                @Field("descricao") String descricao,
                @Field("unidade") String unidade,
                @Field("preco") double preco,
                @Field("foto") String foto,
                @Field("ativo") int ativo,
                @Field("latitude") double latitude,
                @Field("longitude") double longitude,
                Callback<String> callbackCreateProduto
        );

        @FormUrlEncoded()
        @PUT("/produto")
        void updateProduto(
                @Field("id") String codigoBarras,
                @Field("descricao") String descricao,
                @Field("unidade") String unidade,
                @Field("preco") double preco,
                @Field("foto") String foto,
                @Field("ativo") int ativo,
                @Field("latitude") double latitude,
                @Field("longitude") double longitude,
                Callback<String> callbackUpdateProduto
        );


        @DELETE("/produto")
        String deleteProduto(
                @Query("id") String codigoBarras
        );



    }


}