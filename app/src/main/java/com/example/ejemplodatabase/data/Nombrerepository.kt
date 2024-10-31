package com.example.ejemplodatabase.data

class Nombrerepository(private val db : NombreDataBase) {
    suspend fun insertName(nombre : NombreEntity){
        db.nombreDao().insertNombre(nombre)
    }

    suspend fun getNames(): List<NombreEntity> {
        return db.nombreDao().getNombres()
    }
}