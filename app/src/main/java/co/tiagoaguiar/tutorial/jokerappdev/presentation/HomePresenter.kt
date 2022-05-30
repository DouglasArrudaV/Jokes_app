package co.tiagoaguiar.tutorial.jokerappdev.presentation

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import co.tiagoaguiar.tutorial.jokerappdev.data.CategoryRemoteDataSource
import co.tiagoaguiar.tutorial.jokerappdev.data.ListCategoryCallback
import co.tiagoaguiar.tutorial.jokerappdev.model.Category
import co.tiagoaguiar.tutorial.jokerappdev.view.HomeFragment

class HomePresenter(
    private val view: HomeFragment,
    private val dataSource: CategoryRemoteDataSource
) : ListCategoryCallback {

    // INPUT
    fun findAllCategories() {
        view.showProgress()
        dataSource.findAllCategories(this)
        // PRESENTER CHAMA O DATASOURCE, QUE O DATASOURCE CHAMA O PRESENTER DE NOVO
    }

    // output (SUCESSO | FALHA | COMPLETE)
    override fun onSuccess(response: List<String>) {
        val start = 320
        val end = 359
        val diff = (end - start) / response.size

        val categories = response.mapIndexed() { index, s ->
            val hsv = floatArrayOf(
                start + (diff * index).toFloat(), //H, matriz
                100.0f, //S, saturação
                100.0f, //V, valor
            )

            Category(s, Color.HSVToColor(hsv).toLong())
        }

        view.showCategories(categories)
    }

    override fun onError(message: String) {
        view.showFailure(message)
    }

    override fun onComplete() {
        view.hideProgress()
    }

}

// 1. CICLO DE VIDA DO FRAGMENT FAZ A AÇÃO (CHAMAR O PRESENTER PEDINDO PARA BUSCAR AS CATEGORIAS)
// 2. O PRESENTER PEDE A LISTA DE CAT. NO MODEL
// 3. O MODEL DEVOLVE UMA LISTA List<String>
// 4. o PRESENTER FORMATAR ESSA LISTA (String) EM (Category (MODEL))
// 5. VIEW PEGA A LISTA DE List<Category> E CONVERTE PARA O List<CategoryItem>
