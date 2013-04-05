package org.test

import pro.savant.circumflex._, orm._
import java.util.Date

class User
    extends Record[Long, User]
    with IdentityGenerator[Long, User] {

  def PRIMARY_KEY: ValueHolder[Long, User] = id
  def relation: Relation[Long, User] = User

  val id = "id".BIGINT.NOT_NULL.AUTO_INCREMENT
  val username = "username".HTML.NOT_NULL
  val password = "password".TEXT.NOT_NULL
  val signupDate = "signup_date".TIMESTAMP.NOT_NULL(new Date)
  val active = "active".BOOLEAN.NOT_NULL(true)

  val firstName = "first_name".HTML.NOT_NULL
  val lastName = "first_name".HTML.NOT_NULL
}

object User extends User with Table[Long, User] {

  val uniqueUsernameKey = UNIQUE(username)

  validation
      .unique(_.username)
      .notEmpty(_.username)
      .notEmpty(_.password)
      .notEmpty(_.firstName)
      .notEmpty(_.lastName)

  private val u = User AS "u"

  def findByUsername(username: String) =
    SELECT(u.*)
        .FROM(u)
        .WHERE(u.username EQ username)
        .unique()

  def findByName(name: String) =
    SELECT(u.*)
        .FROM(u)
        .WHERE(u.firstName LLIKE(name + "%"))
        .list()
}

class UserKind
    extends Record[Long, UserKind]
    with IdentityGenerator[Long, UserKind] {

  def PRIMARY_KEY: ValueHolder[Long, UserKind] = id
  def relation: Relation[Long, UserKind] = UserKind

  val id = "id".BIGINT.NOT_NULL.AUTO_INCREMENT
  val user = "user_id".BIGINT.NOT_NULL
      .REFERENCES(User).ON_DELETE(CASCADE).ON_UPDATE(CASCADE)

  val kind = "kind".TEXT.NOT_NULL("")
}

object UserKind
    extends UserKind
    with Table[Long, UserKind] {
  val userUniqueKey = UNIQUE(user)
}