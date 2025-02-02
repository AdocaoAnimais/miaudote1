import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.projeto2.miaudote.domain.serialization.BlobDeserializer
import com.projeto2.miaudote.domain.serialization.BlobSerializer
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Blob
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "publicacao")
class Publicacao(
    @Id
    @Column(name = "publicacao_id")
    val publicacaoId: UUID,

    @Column(name = "data_registro")
    val dataRegistro: LocalDate,

    @JsonSerialize(using = BlobSerializer::class)
    @JsonDeserialize(using = BlobDeserializer::class)
    @Column(name = "imageData")
    val imageData: Blob,

    @Column(name = "descricao")
    val descricao: String?,
)