package json

class ResourceHelper {

    static String getAsString(String filename) {
        URL resource = Thread.currentThread().contextClassLoader.getResource(filename)
        return resource.getText('UTF-8')
    }

    static InputStream getAsInputStream(String filename) {
        return Thread.currentThread().contextClassLoader.getResourceAsStream(filename)
    }
}
