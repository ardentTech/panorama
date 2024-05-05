package lexicon

import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json

abstract class LexiconTest {
    protected val json by lazy {
        Json {
            classDiscriminator = "discriminator"
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
        }
    }
}