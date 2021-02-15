package com.hfad.tagalong.tools.api

class URLBuilder {
    private var host = ""
    private var endpoint = ""
    private var toReplace = HashMap<String, String>()
    private var params = HashMap<String, String>()

    fun host(host: String): URLBuilder {
        this.host = host
        return this
    }

    fun endpoint(endpoint: String): URLBuilder {
        this.endpoint = endpoint
        return this
    }


    fun from(host: String, endpoint: String): URLBuilder {
        host(host)
        endpoint(endpoint)
        return this
    }

    fun replace(key: String, value: String): URLBuilder {
        toReplace[key] = value
        return this
    }

    fun replace(key: String, value: Any): URLBuilder {
        toReplace[key] = value.toString()
        return this
    }

    fun param(key: String, value: String): URLBuilder {
        params[key] = value
        return this
    }

    fun param(key: String, value: Any): URLBuilder {
        params[key] = value.toString()
        return this
    }

    fun build(): String {
        var url = host + endpoint
        url = replaceVariables(url)
        url = addParameters(url)
        return url
    }

    private fun replaceVariables(url: String): String {
        if (toReplace.isEmpty()) {
            return url
        }
        var newUrl = url
        toReplace.forEach { (key, value) ->
            newUrl = newUrl.replace("{${key}}", value)
        }
        return newUrl
    }

    private fun addParameters(url: String): String {
        if (params.isEmpty()) {
            return url
        }
        var newUrl = url
        val paramsList = ArrayList<String>()
        params.forEach { (key, value) ->
            paramsList.add("$key=$value")
        }
        newUrl += paramsList.joinToString("&", "?")
        return newUrl
    }
}