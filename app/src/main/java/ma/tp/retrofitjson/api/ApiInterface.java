package ma.tp.retrofitjson.api;

import java.util.List;

import ma.tp.retrofitjson.beans.Compte;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;

public interface ApiInterface {


    // Compte endpoints
    @GET("banque/comptes")
    Call<List<Compte>> getAllComptes();

    @POST("banque/comptes")
    Call<Compte> createCompte(@Body Compte compte);

    @GET("banque/comptes/{id}")
    Call<Compte> getCompteById(@Path("id") Long id);

    @PUT("banque/comptes/{id}")
    Call<Compte> updateCompte(@Path("id") Long id, @Body Compte compte);

    @DELETE("banque/comptes/{id}")
    Call<Void> deleteCompte(@Path("id") Long id);






}
