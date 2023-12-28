import com.example.projectrm.IngredientsModel
import com.example.projectrm.RecipeModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/get-recipe") // Replace with your API endpoint
    fun postIngredients(@Body ingredients: List<IngredientsModel>): Call<RecipeModel>
}