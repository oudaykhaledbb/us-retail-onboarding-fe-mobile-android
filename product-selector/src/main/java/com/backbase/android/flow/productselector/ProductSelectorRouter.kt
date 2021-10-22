package com.backbase.android.flow.productselector

/**
 * Created by Backbase R&D B.V. on 2020-05-29.
 *
 * The exit points for the ProductSelector Journey.
 */
interface ProductSelectorRouter {

    /**
     * Move to the next screen
     */
    fun onProductSelectorFinished(interactionResponse: Any?)

    /**
     * Move to the next screen
     */
    fun showHelpWhichProductToUse()
}