import com.example.projectrm.RecipeModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/data")
    fun getData(): Call<List<RecipeModel>> // Replace YourDataModel with the model representing your data

    companion object {
        fun getData(): Call<List<RecipeModel>> {
            TODO("Not yet implemented")
        }
    }
}
