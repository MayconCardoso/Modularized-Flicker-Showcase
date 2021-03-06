package br.com.unicred.feature.arq

/**
 *
 * A command is something that happen just once like for example:
 *  - The ViewModel send 'a command' to the view to navigate to another screen.
 *
 *  It's like the 'State' of the view, but more used to send, again, 'a command' to the screen.
 *  It's everything the does't change the 'visual state' of the view.
 */
interface ViewCommand