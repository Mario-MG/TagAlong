package com.hfad.tagalong.tools.api

class URLBuilder {
    private var host = ""
    private var endpoint = ""
    private var toReplace = HashMap<String, String>()

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

    fun build(): String {
        var url = host + endpoint
        toReplace.forEach { (key, value) ->
            url = url.replace("{${key}}", value)
        }
        return url
    }
}