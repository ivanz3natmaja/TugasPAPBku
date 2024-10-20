package com.tifd.projectcomposed.Navigation

sealed class Screen (val route: String){
    object Matkul : Screen("Matkul")
    object Tugas : Screen ("Tugas")
    object Profil : Screen ("Profil")
    object Auth: Screen ("Auth")
}