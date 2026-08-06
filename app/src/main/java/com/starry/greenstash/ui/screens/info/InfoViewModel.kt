    fun fetchDolarRate() {
        viewModelScope.launch {
            _isLoadingDolar.value = true
            val rate = withContext(Dispatchers.IO) {
                try {
                    // El endpoint "venezuela" devuelve un objeto, no un arreglo
                    val url = URL("https://ve.dolarapi.com/v1/dolares/venezuela")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("User-Agent", "GreenStash-App")
                    connection.connectTimeout = 8000
                    connection.readTimeout = 8000
                    
                    val response = connection.inputStream.bufferedReader().readText()
                    connection.disconnect()

                    // Corrección: Leer como JSONObject directo
                    val jsonObject = org.json.JSONObject(response)
                    
                    // Extrae el promedio (en DolarApi Venezuela, el nodo raíz ya trae el valor oficial)
                    jsonObject.getDouble("promedio")
                } catch (e: Exception) {
                    e.printStackTrace() // Te permite ver el error exacto en Logcat
                    40.0 // Valor de respaldo si falla la red
                }
            }
            _dolarRate.value = rate
            _isLoadingDolar.value = false
        }
    }
    
