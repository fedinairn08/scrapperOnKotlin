package org.scrapper.entity

import jakarta.persistence.*
import lombok.Builder
import lombok.Getter
import lombok.Setter
import lombok.ToString
import lombok.experimental.Accessors
import java.net.URI
import java.sql.Timestamp

@Entity
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Builder
@Table(name = "link")
class Link() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_seq")
    @SequenceGenerator(name = "link_seq", sequenceName = "link_sequence")
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "url")
    var url: URI? = null

    @ManyToOne
    @JoinColumn(name = "chat")
    var chat: Chat? = null

    @Column(name = "last_update")
    var lastUpdate: Timestamp? = null

    constructor(id: Long?, url: URI?, lastUpdate: Timestamp?) : this() {
        this.id = id
        this.url = url
        this.lastUpdate = lastUpdate
    }

    constructor(id: Long?, url: URI?, chat: Chat?, lastUpdate: Timestamp?) : this() {
        this.id = id
        this.url = url
        this.chat = chat
        this.lastUpdate = lastUpdate
    }
}