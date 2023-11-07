package br.com.alura.panucci.ui.uistate

import br.com.alura.panucci.model.Product

sealed class ProductDetailsUiState {

    object LOADING : ProductDetailsUiState()

    object FAILURE : ProductDetailsUiState()

    data class SUCESS(val product: Product) : ProductDetailsUiState()

}