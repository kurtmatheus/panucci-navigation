package br.com.alura.panucci.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.panucci.dao.ProductDao
import br.com.alura.panucci.ui.uistate.ProductDetailsUiState
import br.com.alura.panucci.ui.uistate.ProductDetailsUiState.LOADING
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val dao: ProductDao = ProductDao()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(LOADING)
    val uiState = _uiState.asStateFlow()

    fun findProductById(id: String) {
        viewModelScope.launch {
            delay(2000)
            _uiState.update { LOADING }
            val dataState = dao.findById(id)?.let { product ->
                ProductDetailsUiState.SUCESS(product)
            } ?: ProductDetailsUiState.FAILURE

            _uiState.update { dataState }
        }
    }

}