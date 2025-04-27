package ponder.potato.io

import ponder.potato.model.Api
import ponder.potato.model.data.Example
import ponder.potato.model.data.NewExample
import pondui.io.ApiStore

class ExampleStore: ApiStore() {
    suspend fun readExample(exampleId: Long) = client.get(Api.Examples, exampleId)
    suspend fun readUserExamples() = client.get(Api.Examples.User)
    suspend fun createExample(newExample: NewExample) = client.post(Api.Examples.Create, newExample)
    suspend fun updateExample(example: Example) = client.update(Api.Examples.Update, example)
    suspend fun deleteExample(exampleId: Long) = client.delete(Api.Examples.Delete, exampleId)
}