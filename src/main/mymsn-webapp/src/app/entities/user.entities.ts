export default class User {
  public username: string;
  public email: string;
  public id: string;

  constructor(username: string, email: string, id: string) {
    this.id = id;
    this.email = email;
    this.username = username;
  }
}
