// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Data.proto

package strategies.proto;

public interface PlayerOrBuilder extends
    // @@protoc_insertion_point(interface_extends:strategies.Player)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string id = 1;</code>
   * @return The id.
   */
  java.lang.String getId();
  /**
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <code>string name = 2;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 2;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>bool isBot = 3;</code>
   * @return The isBot.
   */
  boolean getIsBot();

  /**
   * <code>.strategies.Vector position = 4;</code>
   * @return Whether the position field is set.
   */
  boolean hasPosition();
  /**
   * <code>.strategies.Vector position = 4;</code>
   * @return The position.
   */
  strategies.proto.Vector getPosition();
  /**
   * <code>.strategies.Vector position = 4;</code>
   */
  strategies.proto.VectorOrBuilder getPositionOrBuilder();
}