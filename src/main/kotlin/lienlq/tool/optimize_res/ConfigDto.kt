package lienlq.tool.optimize_res

data class ConfigDto(val maxWidth: Int,
                     val inputFolder: String, val outFolder: String,
                     val isPngToJpg: Boolean = false,
                     val startWithName: String? = null
    )