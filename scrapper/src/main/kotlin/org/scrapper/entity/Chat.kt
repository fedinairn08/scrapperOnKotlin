package org.scrapper.entity

import jakarta.persistence.*
import lombok.*
import lombok.experimental.Accessors

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Builder
@Table(name = "chat")
class Chat() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
    @SequenceGenerator(name = "chat_seq", sequenceName = "chat_sequence")
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "chat_id")
    var chatId: Long? = null

    @OneToMany(mappedBy = "chat", cascade = [CascadeType.ALL], orphanRemoval = true)
    @ToString.Exclude
    var links: MutableList<Link>? = mutableListOf()

    constructor(id: Long?, chatId: Long?, links: MutableList<Link>?) : this() {
        this.id = id
        this.chatId = chatId
        this.links = links
    }
}
