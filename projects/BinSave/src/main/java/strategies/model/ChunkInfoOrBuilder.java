// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Data.proto

package strategies.model;

public interface ChunkInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:strategies.ChunkInfo)
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
   * <code>.strategies.Vector position = 3;</code>
   * @return Whether the position field is set.
   */
  boolean hasPosition();
  /**
   * <code>.strategies.Vector position = 3;</code>
   * @return The position.
   */
  strategies.model.Vector getPosition();
  /**
   * <code>.strategies.Vector position = 3;</code>
   */
  strategies.model.VectorOrBuilder getPositionOrBuilder();

  /**
   * <code>.strategies.Vector size = 4;</code>
   * @return Whether the size field is set.
   */
  boolean hasSize();
  /**
   * <code>.strategies.Vector size = 4;</code>
   * @return The size.
   */
  strategies.model.Vector getSize();
  /**
   * <code>.strategies.Vector size = 4;</code>
   */
  strategies.model.VectorOrBuilder getSizeOrBuilder();

  /**
   * <code>string parentChunk = 5;</code>
   * @return The parentChunk.
   */
  java.lang.String getParentChunk();
  /**
   * <code>string parentChunk = 5;</code>
   * @return The bytes for parentChunk.
   */
  com.google.protobuf.ByteString
      getParentChunkBytes();

  /**
   * <code>repeated string childChunks = 6;</code>
   * @return A list containing the childChunks.
   */
  java.util.List<java.lang.String>
      getChildChunksList();
  /**
   * <code>repeated string childChunks = 6;</code>
   * @return The count of childChunks.
   */
  int getChildChunksCount();
  /**
   * <code>repeated string childChunks = 6;</code>
   * @param index The index of the element to return.
   * @return The childChunks at the given index.
   */
  java.lang.String getChildChunks(int index);
  /**
   * <code>repeated string childChunks = 6;</code>
   * @param index The index of the value to return.
   * @return The bytes of the childChunks at the given index.
   */
  com.google.protobuf.ByteString
      getChildChunksBytes(int index);
}
