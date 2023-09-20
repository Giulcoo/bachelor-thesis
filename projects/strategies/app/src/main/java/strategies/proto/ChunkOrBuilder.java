// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Data.proto

package strategies.proto;

public interface ChunkOrBuilder extends
    // @@protoc_insertion_point(interface_extends:strategies.Chunk)
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
   * <code>repeated .strategies.Player players = 2;</code>
   */
  java.util.List<strategies.proto.Player> 
      getPlayersList();
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  strategies.proto.Player getPlayers(int index);
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  int getPlayersCount();
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  java.util.List<? extends strategies.proto.PlayerOrBuilder> 
      getPlayersOrBuilderList();
  /**
   * <code>repeated .strategies.Player players = 2;</code>
   */
  strategies.proto.PlayerOrBuilder getPlayersOrBuilder(
      int index);

  /**
   * <code>.strategies.Vector position = 3;</code>
   * @return Whether the position field is set.
   */
  boolean hasPosition();
  /**
   * <code>.strategies.Vector position = 3;</code>
   * @return The position.
   */
  strategies.proto.Vector getPosition();
  /**
   * <code>.strategies.Vector position = 3;</code>
   */
  strategies.proto.VectorOrBuilder getPositionOrBuilder();

  /**
   * <code>.strategies.Vector size = 4;</code>
   * @return Whether the size field is set.
   */
  boolean hasSize();
  /**
   * <code>.strategies.Vector size = 4;</code>
   * @return The size.
   */
  strategies.proto.Vector getSize();
  /**
   * <code>.strategies.Vector size = 4;</code>
   */
  strategies.proto.VectorOrBuilder getSizeOrBuilder();
}