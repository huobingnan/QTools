package openq.util

/**
 * 针对字符串的一些常用方法的工具类
 */
class Strings {
    companion object {
        /**
         * 将一个字符串按照任意数量的空格进行分隔
         * @param str 输入字符串
         * @return 分隔完毕之后的字符串数组
         */
        @JvmStatic fun splitStringByAnyBlanks(str: String?): List<String> {
            val result = ArrayList<String>()
            if (str != null) {
                val charArray = str.toCharArray()
                val stringBuilder = StringBuilder()

                for (ch in charArray) {
                    if (ch != ' ') {
                        stringBuilder.append(ch)
                    } else {
                        // 添加到res中
                        if (stringBuilder.isNotEmpty()) {
                            result.add(stringBuilder.toString())
                            stringBuilder.setLength(0)
                        }
                    }
                }
                if (stringBuilder.isNotEmpty()) {
                    result.add(stringBuilder.toString())
                }
            }
            return result
        }
    }
}